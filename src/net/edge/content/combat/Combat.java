package net.edge.content.combat;

import net.edge.content.combat.attack.AttackModifier;
import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.attack.listener.CombatListener;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.hit.CombatTaskData;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.task.Task;
import net.edge.util.Stopwatch;
import net.edge.world.World;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.actor.Actor;

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

    private Stopwatch lastAttacked = new Stopwatch();
    private Stopwatch lastBlocked = new Stopwatch();

    private FightType type;

    private CombatStrategy<? super T> strategy;
    private AttackModifier attackModifier = new AttackModifier();
    private List<CombatListener<? super T>> listeners = new LinkedList<>();

    /** The cache of damage dealt to this controller during combat. */
    private final CombatDamage damageCache = new CombatDamage();

    private List<CombatListener<? super T>> pendingAddition = new LinkedList<>();
    private List<CombatListener<? super T>> pendingRemoval = new LinkedList<>();
    private int[] delays = new int[3];

    private Deque<CombatTaskData<T>> hitQueue = new LinkedList<>();
    private int[] lastHitDelays = new int[4];

    public Combat(T attacker) {
        this.attacker = attacker;
        type = FightType.UNARMED_PUNCH;
    }

    public void attack(Actor defender) {
        this.defender = defender;

        if (strategy == null || !strategy.withinDistance(attacker, defender)) {
            attacker.getMovementQueue().follow(defender);
            return;
        }

        attacker.faceEntity(defender);
        attacker.getMovementQueue().reset();
    }

    public static void update() {
        for (Actor actor : World.get().getActors()) {
            if (actor == null || !actor.getState().equals(EntityState.ACTIVE))
                continue;
            actor.getCombat().tick();
        }
    }

    private void tick() {
        updateListeners();

        for (int index = 0; index < delays.length; index++) {
            if (delays[index] > 0) {
                delays[index]--;
            } else {
                if (defender == null || strategy == null) continue;
                if (strategy.getCombatType().ordinal() != index) continue;
                submitStrategy(defender, strategy);
            }
        }

        for (Iterator<CombatTaskData<T>> it = hitQueue.iterator(); it.hasNext();) {
            CombatTaskData<T> data = it.next();
            if (data.getDefender() != defender) {
                hitTask(data).submit();
                it.remove();
            }
        }

        for (int index = 0, sent = 0; index < lastHitDelays.length; index++) {
            if (lastHitDelays[index] > 0) {
                lastHitDelays[index]--;
            } else if (sent < 2 && sendNextHit()) {
                lastHitDelays[index] = 2;
                sent++;
            }
        }
    }

    private boolean sendNextHit() {
        if (hitQueue.isEmpty()) {
            return false;
        }

        CombatTaskData<T> data = hitQueue.poll();
        hitTask(data).submit();
        return true;
    }

    private void updateListeners() {
        if (!pendingAddition.isEmpty()) {
            pendingAddition.forEach(listeners::add);
            pendingAddition.clear();
        }

        if (!pendingRemoval.isEmpty()) {
            pendingRemoval.forEach(listeners::remove);
            pendingRemoval.clear();
        }
    }

    public boolean submitStrategy(Actor defender, CombatStrategy<? super T> strategy) {
        if (!CombatUtil.canAttack(attacker, defender)) {
            return false;
        }

        if (!strategy.withinDistance(attacker, defender)) {
            return false;
        }

        if (!strategy.canAttack(attacker, defender)) {
            return false;
        }

        strategy.getModifier(attacker).ifPresent(this::addModifier);
        listeners.forEach(attack -> attack.getModifier(attacker).ifPresent(this::addModifier));

        submitHits(defender, strategy, strategy.getHits(attacker, defender));

        int delayIndex = strategy.getCombatType().ordinal();
        setDelay(delayIndex, strategy.getAttackDelay(attacker, defender, type));
        return true;
    }

    private void submitHits(Actor defender, CombatStrategy<? super T> strategy, CombatHit... hits) {
        boolean first = true;
        for (CombatHit hit : hits) {
            CombatTaskData<T> data = new CombatTaskData<>(attacker, defender, hit, strategy, first);
            first = false;

            if (data.isFirstHit()) {
                start(defender, strategy, hits);
            }

            attack(defender, hit, strategy);
            hitQueue.add(data);
        }
    }

    public void submitHits(Actor defender, CombatHit... hits) {
        submitHits(defender, strategy, hits);
    }

    private void setDelay(int index, int delay) {
        for (int idx = 0; idx < delays.length; idx++) {
            if (idx != index) {
                delays[idx] += 2;
            } else if (delays[idx] < delay) {
                delays[idx] = delay;
            }
        }
    }

    private void start(Actor defender, CombatStrategy<? super T> strategy, Hit... hits) {
        if (!CombatUtil.canAttack(attacker, defender)) {
            hitQueue.removeIf(_hit -> _hit.getDefender() == defender);
            reset();
            return;
        }

        strategy.start(attacker, defender, hits);
        listeners.forEach(listener -> listener.start(attacker, defender, hits));
    }

    private void attack(Actor defender, Hit hit, CombatStrategy<? super T> strategy) {
        if (!CombatUtil.canAttack(attacker, defender)) {
            hitQueue.removeIf(_hit -> _hit.getDefender() == defender);
            reset();
            return;
        }

        lastDefender = defender;
        lastAttacked.reset();

        strategy.attack(attacker, defender, hit);
        listeners.forEach(listener -> listener.attack(attacker, defender, hit));
    }

    private void hit(Actor defender, Hit hit, CombatStrategy<? super T> strategy) {
        if (!CombatUtil.canAttack(attacker, defender)) {
            hitQueue.removeIf(_hit -> _hit.getDefender() == defender);
            reset();
            return;
        }

//        if (defender.getCombat().getDefender() == null && defender.isAutoRetaliate()) {
//            defender.getCombat().attack(attacker);
//        }

        if (strategy.getCombatType() != CombatType.MAGIC || defender.isMob()) {
            defender.animation(CombatUtil.getBlockAnimation(defender));
        }

        strategy.hit(attacker, defender, hit);
        listeners.forEach(listener -> listener.hit(attacker, defender, hit));
    }

    private void hitsplat(Actor defender, Hit hit, CombatStrategy<? super T> strategy) {
        if (!CombatUtil.canAttack(attacker, defender)) {
            hitQueue.removeIf(_hit -> _hit.getDefender() == defender);
            reset();
            return;
        }

        strategy.hitsplat(attacker, defender, hit);
        listeners.forEach(listener -> listener.hitsplat(attacker, defender, hit));

        defender.getCombat().block(attacker, hit, strategy.getCombatType());
        defender.getCombat().getDamageCache().add(attacker, hit.getDamage());

        if (strategy.getCombatType() != CombatType.MAGIC || hit.isAccurate()) {
            defender.damage(hit);

            if (defender.getCurrentHealth() <= 0) {
                defender.getCombat().onDeath(defender, hit);
            }
        }
    }

    public void block(Actor attacker, Hit hit, CombatType combatType) {
        T defender = this.attacker;
        lastBlocked.reset();
        lastAttacker = attacker;
        listeners.forEach(attack -> attack.block(attacker, defender, hit, combatType));
    }

    private void onDeath(Actor attacker, Hit hit) {
        T defender = this.attacker;
        listeners.forEach(attack -> attack.onDeath(attacker, defender, hit));
        defender.getMovementQueue().reset();
        attacker.getCombat().reset();
        defender.getCombat().reset();
    }

    private void finish(Actor defender, CombatStrategy<? super T> strategy) {
        if (!CombatUtil.canAttack(attacker, defender)) {
            hitQueue.removeIf(hit -> hit.getDefender() == defender);
            reset();
            return;
        }

        strategy.finish(attacker, defender);
        strategy.getModifier(attacker).ifPresent(attacker.getCombat()::removeModifier);

        listeners.forEach(attack -> {
            attack.finish(attacker, defender);
            attack.getModifier(attacker).ifPresent(attacker.getCombat()::removeModifier);
        });
    }

    public void reset() {
        defender = null;
        attacker.getMovementQueue().reset();
    }

    public void addModifier(AttackModifier modifier) {
        attackModifier.add(modifier);
    }

    public void removeModifier(AttackModifier modifier) {
        attackModifier.remove(modifier);
    }

    public void addListener(CombatListener<? super T> attack) {
        if (listeners.contains(attack) || pendingAddition.contains(attack)) {
            return;
        }

        pendingAddition.add(attack);
    }

    public void removeListener(CombatListener<? super T> attack) {
        if (!listeners.contains(attack) || pendingRemoval.contains(attack)) {
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
        return defender != null && lastDefender == defender && !stopwatchElapsed(lastAttacked, CombatConstants.COMBAT_TIMER);
    }

    public boolean isUnderAttackBy(Actor attacker) {
        return attacker != null && lastAttacker == attacker && !stopwatchElapsed(lastBlocked, CombatConstants.COMBAT_TIMER);
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

    private Task hitTask(CombatTaskData<T> data) {
        return new Task(data.getHitDelay(), data.getHitDelay() == 0) {
            @Override
            protected void execute() {
                hit(data.getDefender(), data.getHit(), data.getStrategy());
                hitsplatTask(data).submit();
                cancel();
            }
        };
    }

    private Task hitsplatTask(CombatTaskData<T> data) {
        return new Task(data.getHitsplatDelay(), data.getHitsplatDelay() == 0) {
            @Override
            protected void execute() {
                hitsplat(data.getDefender(), data.getHit(), data.getStrategy());

                if (data.isFirstHit()) {
                    finish(data.getDefender(), data.getStrategy());
                }

                cancel();
            }
        };
    }

    public boolean hasPassed(int delay) {
        return stopwatchElapsed(lastAttacked, delay) || stopwatchElapsed(lastBlocked, delay);
    }

    private static boolean stopwatchElapsed(Stopwatch stopwatch, int seconds) {
        return stopwatch.elapsed(seconds, TimeUnit.SECONDS);
    }

    public long elapsedTime() {
        long elapsed = lastAttacked.elapsedTime();
        return lastBlocked.elapsedTime() > elapsed ? elapsed : lastBlocked.elapsedTime();
    }
}
