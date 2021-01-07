package com.rageps.world.entity.actor.combat;

import com.rageps.net.packet.out.SendCombatTarget;
import com.rageps.net.refactor.packet.out.model.CombatTargetPacket;
import com.rageps.world.World;
import com.rageps.world.entity.actor.combat.attack.FightType;
import com.rageps.combat.listener.CombatListener;
import com.rageps.world.entity.actor.combat.formula.CombatFormula;
import com.rageps.world.entity.actor.combat.formula.FormulaModifier;
import com.rageps.world.entity.actor.combat.hit.CombatData;
import com.rageps.world.entity.actor.combat.hit.CombatHit;
import com.rageps.world.entity.actor.combat.hit.Hit;
import com.rageps.combat.strategy.CombatStrategy;
import com.rageps.task.Task;
import com.rageps.util.Stopwatch;
import com.rageps.world.entity.actor.Actor;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class Combat<T extends Actor> {
	
	private final T attacker;
	private Actor defender;
	
	private Actor lastAttacker;
	private Actor lastDefender;
	
	private final Stopwatch lastAttacked = new Stopwatch();
	private final Stopwatch lastBlocked = new Stopwatch();

	private FightType type;

	private final CombatFormula<T> formula = new CombatFormula<>();
	private final Deque<CombatListener<? super T>> listeners = new LinkedList<>();
	private final Deque<CombatListener<? super T>> pendingAddition = new LinkedList<>();
	private final Deque<CombatListener<? super T>> pendingRemoval = new LinkedList<>();
	
	private final CombatDamage damageCache = new CombatDamage();
	private final Deque<CombatData<T>> combatQueue = new LinkedList<>();
	private final Deque<Hit> damageQueue = new LinkedList<>();
	
	private final int[] hitsplatCooldowns = new int[4];
	private int cooldown;
	private boolean within;
	
	public Combat(T attacker) {
		this.attacker = attacker;
		type = FightType.UNARMED_PUNCH;
		lastAttacked.run();
		lastBlocked.run();
	}
	
	public void attack(Actor defender) {
		if(defender == null) {
			return;
		}
		
		CombatStrategy<? super T> strategy = attacker.getStrategy();
		within = strategy != null && strategy.withinDistance(attacker, defender);
		
		if(!CombatUtil.canAttack(attacker, defender)) {
			return;
		}
		
		this.defender = defender;
		this.lastDefender = defender;
		attacker.faceEntity(defender);
		attacker.getMovementQueue().follow(defender);
	}
	
	public void tick() {
		updateListeners();
		
		if(cooldown > 0) {
			cooldown--;
		}
		
		if(cooldown == 0 && defender != null) {
			CombatStrategy<? super T> strategy = attacker.getStrategy();
			submitStrategy(defender, strategy);
		}
		
		while(!combatQueue.isEmpty()) {
			CombatData<T> data = combatQueue.poll();
			hitTask(data).submit();
		}
		
		for(int index = 0, sent = 0; index < hitsplatCooldowns.length; index++) {
			if(hitsplatCooldowns[index] > 0) {
				hitsplatCooldowns[index]--;
			} else if(sent < 2 && sendNextHitsplat()) {
				hitsplatCooldowns[index] = 2;
				sent++;
			}
		}
		if(attacker.isPlayer() && attacker.getCombat().getDefender() != null)
		if (defender != null && defender.getCombat().getDefender() != null && attacker.isPlayer()) {
		attacker.toPlayer().send(new CombatTargetPacket(defender));
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
		if(!pendingAddition.isEmpty()) {
			for(Iterator<CombatListener<? super T>> iterator = pendingAddition.iterator(); iterator.hasNext(); ) {
				CombatListener<? super T> next = iterator.next();
				addModifier(next);
				listeners.add(next);
				iterator.remove();
			}
		}
		
		if(!pendingRemoval.isEmpty()) {
			for(Iterator<CombatListener<? super T>> iterator = pendingRemoval.iterator(); iterator.hasNext(); ) {
				CombatListener<? super T> next = iterator.next();
				listeners.remove(next);
				removeModifier(next);
				iterator.remove();
			}
		}
	}
	
	public void submitStrategy(Actor defender, CombatStrategy<? super T> strategy) {
		if(!canAttack(defender, strategy)) {
			return;
		}
		
		if(!checkWithin(attacker, defender, strategy)) {
			return;
		}
		
		formula.add(strategy);
		cooldown(strategy.getAttackDelay(attacker, defender, type));
		submitHits(defender, strategy, strategy.getHits(attacker, defender));
		formula.remove(strategy);
	}
	
	private void submitHits(Actor defender, CombatStrategy<? super T> strategy, CombatHit... hits) {
		start(defender, strategy, hits);
		for(int index = 0; index < hits.length; index++) {
			boolean last = index == hits.length - 1;
			CombatHit hit = hits[index];
			CombatData<T> data = new CombatData<>(attacker, defender, hit, strategy, last);
			attack(defender, hit, strategy);
			combatQueue.add(data);
		}
	}
	
	public void submitHits(Actor defender, CombatHit... hits) {
		submitHits(defender, attacker.getStrategy(), hits);
	}
	
	public void queueDamage(Hit hit) {
		if(attacker.getCurrentHealth() <= 0) {
			return;
		}
		
		if(damageQueue.size() > 10) {
			attacker.decrementHealth(hit);
			return;
		}
		
		damageQueue.add(hit);
	}
	
	public void cooldown(int delay) {
		if(cooldown < delay)
			cooldown = delay;
	}
	
	public void setCooldown(int delay) {
		cooldown = delay;
	}
	
	private boolean canAttack(Actor defender, CombatStrategy<? super T> strategy) {
		if(!CombatUtil.validateMobs(attacker, defender)) {
			return false;
		}
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
		return defender.getCombat().canOtherAttack(attacker);
	}
	
	private boolean canOtherAttack(Actor attacker) {
		T defender = this.attacker;
		for(CombatListener<? super T> listener : listeners) {
			if(!listener.canOtherAttack(attacker, defender)) {
				return false;
			}
		}
		return defender.getStrategy().canOtherAttack(attacker, defender);
	}
	
	private void start(Actor defender, CombatStrategy<? super T> strategy, Hit... hits) {
		if(!CombatUtil.validateMobs(attacker, defender)) {
			combatQueue.removeIf(_hit -> _hit.getDefender() == defender);
			if(defender.isTeleporting())
				defender.getCombat().damageQueue.clear();
			reset(true, true);
			return;
		}
		
		strategy.start(attacker, defender, hits);
		listeners.forEach(listener -> listener.start(attacker, defender, hits));
	}
	
	private void attack(Actor defender, Hit hit, CombatStrategy<? super T> strategy) {
		if(!CombatUtil.validateMobs(attacker, defender)) {
			combatQueue.removeIf(_hit -> _hit.getDefender() == defender);
			if(defender.isTeleporting())
				defender.getCombat().damageQueue.clear();
			reset(true, true);
			return;
		}
		
		lastDefender = defender;
		lastAttacked.reset();
		
		strategy.attack(attacker, defender, hit);
		listeners.forEach(listener -> listener.attack(attacker, defender, hit));
	}
	
	private void block(Actor attacker, Hit hit, CombatType combatType) {
		T defender = this.attacker;
		lastBlocked.reset();
		lastAttacker = attacker;
		listeners.forEach(listener -> listener.block(attacker, defender, hit, combatType));
		defender.getStrategy().block(attacker, defender, hit, combatType);
		retaliate(attacker);
	}
	
	private void hit(Actor defender, Hit hit, CombatStrategy<? super T> strategy) {
		if(!CombatUtil.validateMobs(attacker, defender)) {
			combatQueue.removeIf(_hit -> _hit.getDefender() == defender);
			if(defender.isTeleporting())
				defender.getCombat().damageQueue.clear();
			reset(true, true);
			return;
		}
		
		strategy.hit(attacker, defender, hit);
		listeners.forEach(listener -> listener.hit(attacker, defender, hit));
		
		if(defender.getCurrentHealth() - hit.getDamage() > 0) {
			defender.getCombat().block(attacker, hit, strategy.getCombatType());
		}
		
		if(!strategy.getCombatType().equals(CombatType.MAGIC)) {
			defender.animation(CombatUtil.getBlockAnimation(defender));
		}
	}
	
	private void hitsplat(Actor defender, Hit hit, CombatStrategy<? super T> strategy) {
		if(!CombatUtil.validateMobs(attacker, defender)) {
			combatQueue.removeIf(_hit -> _hit.getDefender() == defender);
			if(defender.isTeleporting())
				defender.getCombat().damageQueue.clear();
			reset(true, true);
			return;
		}
		
		strategy.hitsplat(attacker, defender, hit);
		listeners.forEach(listener -> listener.hitsplat(attacker, defender, hit));
		
		if(!strategy.getCombatType().equals(CombatType.MAGIC) || hit.isAccurate()) {
			if(defender.getCurrentHealth() > 0 && hit.getDamage() >= 0) {
				defender.getCombat().queueDamage(hit);
				defender.getCombat().damageCache.add(attacker, hit);
			}
		}
	}
	
	public void preDeath(Actor attacker, Hit hit) {
		if(attacker != null) {
			T defender = this.attacker;
			defender.getStrategy().preDeath(attacker, defender, hit);
			listeners.forEach(listener -> listener.preDeath(attacker, defender, hit));
			reset(true, true);
		}
	}
	
	public void preKill(Actor defender, Hit hit) {
		if(attacker != null) {
			listeners.forEach(listener -> listener.preKill(attacker, defender, hit));
		}
	}
	
	public void onKill(Actor defender, Hit hit) {
		if(attacker != null) {
			listeners.forEach(listener -> listener.onKill(attacker, defender, hit));
		}
	}
	
	public void onDeath(Actor attacker, Hit hit) {
		if(attacker != null) {
			T defender = this.attacker;
			listeners.forEach(listener -> listener.onDeath(attacker, defender, hit));
			defender.getMovementQueue().reset();
			reset(true, true);
		}
	}
	
	private void retaliate(Actor attacker) {
		T defender = this.attacker;
		
		if(this.defender != null && this.defender.same(defender)) {
			return;
		}
		
		if(!CombatUtil.canAttack(defender, attacker)) {
			return;
		}
		
		if(!defender.getMovementQueue().isMovementDone()) {
			return;
		}
		
		if(this.defender == null && defender.isAutoRetaliate() || defender.getStrategy().hitBack()) {
			defender.getCombat().attack(attacker);
		} else if(defender.isMob() && !defender.getCombat().isUnderAttack()) {
			defender.getCombat().attack(attacker);
		}
	}
	
	private void finishIncoming(Actor attacker) {
		T defender = this.attacker;
		defender.getStrategy().finishIncoming(attacker, defender);
		listeners.forEach(listener -> listener.finishIncoming(attacker, defender));
	}
	
	private void finishOutgoing(Actor defender, CombatStrategy<? super T> strategy) {
		strategy.finishOutgoing(attacker, defender);
		listeners.forEach(listener -> listener.finishOutgoing(attacker, defender));
		defender.getCombat().finishIncoming(attacker);
	}

	public void reset(boolean fullCombat, boolean resetWalk) {
		if(attacker.isPlayer())
			attacker.toPlayer().send(new CombatTargetPacket(null));

		if(defender != null) {
			if(fullCombat) {
				Actor def = defender;
				defender = null;
				def.getCombat().reset(false, true);
				} else {
				defender = null;
			}
			attacker.faceEntity(null);
			attacker.setFollowing(false);
		}
		if(resetWalk) {
			attacker.getMovementQueue().reset();
		}
	}
	
	public void addModifier(FormulaModifier<? super T> modifier) {
		formula.add(modifier);
	}
	
	public void removeModifier(FormulaModifier<? super T> modifier) {
		formula.remove(modifier);
	}
	
	public void addListener(CombatListener<? super T> attack) {
		if(listeners.contains(attack) || pendingAddition.contains(attack) || attack == null) {
			if(attack == null)
				World.getLogger().warn("Trying to add null combat listener", new Exception());
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
		return defender != null && !stopwatchElapsed(lastAttacked, CombatConstants.COMBAT_TIMER);
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
	
	public FightType getFightType() {
		return type;
	}
	
	public void setFightType(FightType type) {
		this.type = type;
	}
	
	public boolean checkWithin() {
		if(defender == null) {
			return false;
		}
		
		CombatStrategy<? super T> strategy = attacker.getStrategy();
		return checkWithin(attacker, defender, strategy);
	}
	
	private boolean checkWithin(T attacker, Actor defender, CombatStrategy<? super T> strategy) {
		if(strategy == null) {
			within = false;
			return false;
		}
		
		if(!strategy.withinDistance(attacker, defender)) {
			within = false;
			return false;
		}
		
		for(CombatListener<? super T> listener : listeners) {
			if(!listener.withinDistance(attacker, defender)) {
				within = false;
				return false;
			}
		}
		
		within = true;
		return true;
	}
	
	public boolean isWithin() {
		return within;
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
					finishOutgoing(data.getDefender(), data.getStrategy());
				}
				
				cancel();
			}
		};
	}
	
	public boolean hasPassed(int delay) {
		return elapsedTime() - delay >= 0;
	}
	
	private static boolean stopwatchElapsed(Stopwatch stopwatch, int seconds) {
		return stopwatch.elapsed(seconds, TimeUnit.SECONDS);
	}
	
	public long elapsedTime() {
		long attacked = lastAttacked.elapsedTime();
		long blocked = lastBlocked.elapsedTime();
		return Math.max(attacked, blocked);
	}
	
	public int modifyAttackLevel(Actor defender, int level) {
		return formula.modifyAttackLevel(attacker, defender, level);
	}
	
	public int modifyStrengthLevel(Actor defender, int level) {
		return formula.modifyStrengthLevel(attacker, defender, level);
	}
	
	public int modifyDefenceLevel(Actor attacker, int level) {
		return formula.modifyDefenceLevel(attacker, this.attacker, level);
	}
	
	public int modifyRangedLevel(Actor defender, int level) {
		return formula.modifyRangedLevel(attacker, defender, level);
	}
	
	public int modifyMagicLevel(Actor defender, int level) {
		return formula.modifyMagicLevel(attacker, defender, level);
	}
	
	public int modifyAccuracy(Actor defender, int roll) {
		return formula.modifyAccuracy(attacker, defender, roll);
	}
	
	public int modifyDefensive(Actor attacker, int roll) {
		return formula.modifyDefensive(attacker, this.attacker, roll);
	}
	
	public int modifyDamage(Actor defender, int damage) {
		return formula.modifyDamage(attacker, defender, damage);
	}
	
	public int modifyOffensiveBonus(Actor defender, int bonus) {
		return formula.modifyOffensiveBonus(attacker, defender, bonus);
	}
	
	public int modifyAggresiveBonus(Actor defender, int bonus) {
		return formula.modifyAggressiveBonus(attacker, defender, bonus);
	}
	
	public int modifyDefensiveBonus(Actor attacker, int bonus) {
		return formula.modifyDefensiveBonus(attacker, this.attacker, bonus);
	}
	
	public void resetTimers() {
		lastBlocked.reset(-CombatConstants.COMBAT_TIMER, TimeUnit.SECONDS);
		lastAttacked.reset(-CombatConstants.COMBAT_TIMER, TimeUnit.SECONDS);
	}
	
}
