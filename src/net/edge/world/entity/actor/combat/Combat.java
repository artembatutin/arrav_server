package net.edge.world.entity.actor.combat;

import net.edge.task.Task;
import net.edge.util.Stopwatch;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.attack.AttackModifier;
import net.edge.world.entity.actor.combat.attack.FightType;
import net.edge.world.entity.actor.combat.attack.listener.CombatListener;
import net.edge.world.entity.actor.combat.hit.CombatData;
import net.edge.world.entity.actor.combat.hit.CombatHit;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.strategy.CombatStrategy;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Combat<T extends Actor> {
	
	private final T attacker;
	private Actor defender;
	
	private Actor lastAttacker;
	private Actor lastDefender;
	
	private final Stopwatch lastAttacked = new Stopwatch();
	private final Stopwatch lastBlocked = new Stopwatch();
	private FightType type;
	
	private CombatStrategy<? super T> strategy;
	private final AttackModifier attackModifier = new AttackModifier();
	private final List<CombatListener<? super T>> listeners = new LinkedList<>();
	private final Deque<CombatListener<? super T>> pendingAddition = new LinkedList<>();
	private final Deque<CombatListener<? super T>> pendingRemoval = new LinkedList<>();
	
	private final CombatDamage damageCache = new CombatDamage();
	private final Deque<CombatData<T>> combatQueue = new LinkedList<>();
	private final Deque<Hit> damageQueue = new LinkedList<>();
	
	private final int[] hitsplatDelays = new int[4];
	private final int[] combatDelays = new int[3];
	
	public Combat(T attacker) {
		this.attacker = attacker;
		type = FightType.UNARMED_PUNCH;
	}
	
	public void attack(Actor defender) {
		this.defender = defender;
		attacker.getMovementQueue().follow(defender);
		attacker.setFollowing(true);
	}
	
	public void tick() {
		updateListeners();
		
		for(int index = 0; index < combatDelays.length; index++) {
			if(combatDelays[index] > 0) {
				combatDelays[index]--;
			} else {
				if(defender == null || strategy == null)
					continue;
				if(strategy.getCombatType().ordinal() != index)
					continue;
				submitStrategy(defender, strategy);
			}
		}
		
		while(!combatQueue.isEmpty()) {
			CombatData<T> data = combatQueue.poll();
			hitTask(data).submit();
		}
		
		for(int index = 0, sent = 0; index < hitsplatDelays.length; index++) {
			if(hitsplatDelays[index] > 0) {
				hitsplatDelays[index]--;
			} else if(sent < 2 && sendNextHitsplat()) {
				hitsplatDelays[index] = 2;
				sent++;
			}
		}
	}
	
	private boolean sendNextHitsplat() {
		if(damageQueue.isEmpty()) {
			return false;
		}
		
		if(attacker.isDead() || attacker.isNeedsPlacement() || attacker.isTeleporting()) {
			damageQueue.clear();
			return false;
		}
		
		Hit hit = damageQueue.poll();
		attacker.writeDamage(hit);
		return true;
	}
	
	private void updateListeners() {
		if (pendingAddition.isEmpty() && pendingRemoval.isEmpty()) {
			return;
		}

		Iterator<CombatListener<? super T>> iterator;

		for (iterator = pendingAddition.iterator(); iterator.hasNext();) {
			listeners.add(iterator.next());
			iterator.remove();
		}

		for (iterator = pendingRemoval.iterator(); iterator.hasNext();) {
			listeners.remove(iterator.next());
			iterator.remove();
		}
	}
	
	public boolean submitStrategy(Actor defender, CombatStrategy<? super T> strategy) {
		if(!canAttack(defender)) {
			return false;
		}
		
		if(!strategy.withinDistance(attacker, defender)) {
			attacker.getMovementQueue().follow(defender);
			attacker.setFollowing(true);
			return false;
		}
		
		strategy.getModifier(attacker).ifPresent(this::addModifier);
		listeners.forEach(listener -> listener.getModifier(attacker).ifPresent(this::addModifier));
		
		submitHits(defender, strategy, strategy.getHits(attacker, defender));
		
		int delayIndex = strategy.getCombatType().ordinal();
		setDelay(delayIndex, strategy.getAttackDelay(attacker, defender, type));
		return true;
	}
	
	private void submitHits(Actor defender, CombatStrategy<? super T> strategy, CombatHit... hits) {
		for(int index = 0; index < hits.length; index++) {
			CombatHit hit = hits[index];

			CombatData<T> data = new CombatData<>(attacker, defender, hit, strategy, index == hits.length - 1);
			if(data.isLastHit()) {
				start(defender, strategy, hits);
			}
			
			attack(defender, hit, strategy);
			combatQueue.add(data);
		}
	}
	
	public void submitHits(Actor defender, CombatHit... hits) {
		submitHits(defender, strategy, hits);
	}
	
	public void queueDamage(Hit hit) {
		if(damageQueue.size() >= 8) {
			return;
		}
		
		if(hit.getDamage() < 0) {
			damageQueue.addFirst(hit);
		} else {
			damageQueue.addLast(hit);
		}
	}
	
	private void setDelay(int index, int delay) {
		for(int idx = 0; idx < combatDelays.length; idx++) {
			if(idx != index) {
				combatDelays[idx] += 2;
			} else if(combatDelays[idx] < delay) {
				combatDelays[idx] = delay;
			}
		}
	}
	
	private boolean canAttack(Actor defender) {
		if(!CombatUtil.canAttack(attacker, defender)) {
			return false;
		}
		
		if(!strategy.canAttack(attacker, defender)) {
			return false;
		}
		
		for(CombatListener<? super T> listener : listeners) {
			if(!listener.canAttack(attacker, defender)) {
				return false;
			}
		}
		
		return true;
	}
	
	private void start(Actor defender, CombatStrategy<? super T> strategy, Hit... hits) {
		if(!CombatUtil.canAttack(attacker, defender)) {
			combatQueue.removeIf(_hit -> _hit.getDefender() == defender);
			defender.getCombat().damageQueue.clear();
			reset();
			return;
		}
		
		strategy.start(attacker, defender, hits);
		listeners.forEach(listener -> listener.start(attacker, defender, hits));
	}
	
	private void attack(Actor defender, Hit hit, CombatStrategy<? super T> strategy) {
		if(!CombatUtil.canAttack(attacker, defender)) {
			combatQueue.removeIf(_hit -> _hit.getDefender() == defender);
			defender.getCombat().damageQueue.clear();
			reset();
			return;
		}
		
		lastDefender = defender;
		lastAttacked.reset();
		
		strategy.attack(attacker, defender, hit);
		listeners.forEach(listener -> listener.attack(attacker, defender, hit));
	}
	
	private void hit(Actor defender, Hit hit, CombatStrategy<? super T> strategy) {
		if(!CombatUtil.canAttack(attacker, defender)) {
			combatQueue.removeIf(_hit -> _hit.getDefender() == defender);
			defender.getCombat().damageQueue.clear();
			reset();
			return;
		}

		if(defender.getCombat().getDefender() == null && defender.isAutoRetaliate()) {
			defender.getCombat().attack(attacker);
		}

		defender.getCombat().block(attacker, hit, strategy.getCombatType());
		if(strategy.getCombatType() != CombatType.MAGIC || defender.isMob()) {
			defender.animation(CombatUtil.getBlockAnimation(defender));
		}
		
		strategy.hit(attacker, defender, hit);
		listeners.forEach(listener -> listener.hit(attacker, defender, hit));
	}
	
	private void hitsplat(Actor defender, Hit hit, CombatStrategy<? super T> strategy) {
		if(!CombatUtil.canAttack(attacker, defender)) {
			combatQueue.removeIf(_hit -> _hit.getDefender() == defender);
			defender.getCombat().damageQueue.clear();
			reset();
			return;
		}

		listeners.forEach(listener -> listener.hitsplat(attacker, defender, hit));

		if (strategy.getCombatType() != CombatType.MAGIC || hit.isAccurate()) {
			defender.getCombat().queueDamage(hit);
			defender.getCombat().damageCache.add(attacker, hit.getDamage());
			
			if(defender.getCurrentHealth() <= 0) {
				defender.getCombat().onDeath(attacker, hit);
			}
		}
	}
	
	private void finishAttacker(Actor attacker) {
		T defender = this.attacker;
		listeners.forEach(listener -> listener.finishAttacker(attacker, defender));
	}

	private void block(Actor attacker, Hit hit, CombatType combatType) {
		T defender = this.attacker;
		lastBlocked.reset();
		lastAttacker = attacker;
		listeners.forEach(listener -> listener.block(attacker, defender, hit, combatType));
	}

	private void onDeath(Actor attacker, Hit hit) {
		T defender = this.attacker;
		listeners.forEach(listener -> listener.onDeath(attacker, defender, hit));
		defender.getMovementQueue().reset();
		attacker.getCombat().reset();
		reset();
	}
	
	private void finish(Actor defender, CombatStrategy<? super T> strategy) {
		strategy.finish(attacker, defender);
		strategy.getModifier(attacker).ifPresent(attacker.getCombat()::removeModifier);
		
		listeners.forEach(listener -> {
			listener.finish(attacker, defender);
			listener.getModifier(attacker).ifPresent(attacker.getCombat()::removeModifier);
		});

		defender.getCombat().finishAttacker(defender);
	}
	
	public void reset() {
		defender = null;
		attacker.faceEntity(null);
		attacker.getMovementQueue().reset();
	}
	
	public void addModifier(AttackModifier modifier) {
		attackModifier.add(modifier);
	}
	
	public void removeModifier(AttackModifier modifier) {
		attackModifier.remove(modifier);
	}
	
	public void addListener(CombatListener<? super T> attack) {
		if(listeners.contains(attack) || pendingAddition.contains(attack)) {
			return;
		}
		pendingAddition.add(attack);
	}
	
	public void removeListener(CombatListener<? super T> attack) {
		if(!listeners.contains(attack) || pendingRemoval.contains(attack)) {
			return;
		}
		pendingRemoval.add(attack);
	}
	
	public boolean inCombat() {
		return isAttacking() || isUnderAttack();
	}
	
	public boolean isAttacking() {
		return lastDefender != null && !stopwatchElapsed(lastAttacked, CombatConstants.COMBAT_TIMER);
	}
	
	public boolean isUnderAttack() {
		return lastAttacker != null && !stopwatchElapsed(lastBlocked, CombatConstants.COMBAT_TIMER);
	}
	
	public boolean isAttacking(Actor defender) {
		return defender != null && defender.same(lastDefender) && !stopwatchElapsed(lastAttacked, CombatConstants.COMBAT_TIMER);
	}
	
	public boolean isUnderAttackBy(Actor attacker) {
		return attacker != null && attacker.same(lastAttacker) && !stopwatchElapsed(lastBlocked, CombatConstants.COMBAT_TIMER);
	}
	
	public double getAccuracyModifier() {
		return attackModifier.getAccuracy();
	}
	
	public double getAggressiveModifier() {
		return attackModifier.getAggressive();
	}
	
	public double getDefensiveModifier() {
		return attackModifier.getDefensive();
	}
	
	public double getDamageModifier() {
		return attackModifier.getDamage();
	}
	
	public FightType getFightType() {
		return type;
	}
	
	public void setFightType(FightType type) {
		this.type = type;
	}
	
	public CombatStrategy<? super T> getStrategy() {
		return strategy;
	}
	
	public void setStrategy(CombatStrategy<? super T> next) {
		strategy = next;
	}
	
	public Actor getDefender() {
		return defender;
	}
	
	public CombatDamage getDamageCache() {
		return damageCache;
	}
	
	public Actor getLastAttacker() {
		return lastAttacker;
	}
	
	public Actor getLastDefender() {
		return lastDefender;
	}
	
	private Task hitTask(CombatData<T> data) {
		return new Task(data.getHitDelay(), data.getHitDelay() == 0) {
			@Override
			protected void execute() {
				hit(data.getDefender(), data.getHit(), data.getStrategy());
				hitsplatTask(data).submit();
				cancel();
			}
		};
	}
	
	private Task hitsplatTask(CombatData<T> data) {
		return new Task(data.getHitsplatDelay(), data.getHitsplatDelay() == 0) {
			@Override
			protected void execute() {
				hitsplat(data.getDefender(), data.getHit(), data.getStrategy());

				if(data.isLastHit()) {
					finish(data.getDefender(), data.getStrategy());
				}

				cancel();
			}
		};
	}
	
	public boolean hasPassed(int delay) {
		return stopwatchElapsed(lastAttacked, delay) && stopwatchElapsed(lastBlocked, delay);
	}
	
	private static boolean stopwatchElapsed(Stopwatch stopwatch, int seconds) {
		return stopwatch.elapsed(seconds, TimeUnit.SECONDS);
	}
	
	public long elapsedTime() {
		long attacked = lastAttacked.elapsedTime();
		long blocked = lastBlocked.elapsedTime();
		return blocked > attacked ? attacked : blocked;
	}

}
