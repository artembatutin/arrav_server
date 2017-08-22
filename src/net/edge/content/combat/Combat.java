package net.edge.content.combat;

import net.edge.content.combat.attack.AttackModifier;
import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.attack.listener.CombatListener;
import net.edge.content.combat.events.CombatChain;
import net.edge.content.combat.events.CombatEvent;
import net.edge.content.combat.events.CombatEventManager;
import net.edge.content.combat.events.impl.*;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.task.Task;
import net.edge.util.Stopwatch;
import net.edge.world.World;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.actor.Actor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Combat<T extends Actor> {
    private final T attacker;
    private   Actor defender;

    private Actor lastAttacker;
    private Actor lastDefender;

    private Stopwatch lastAttacked = new Stopwatch();
    private Stopwatch lastBlocked = new Stopwatch();

    private FightType type;

    private CombatStrategy<? super T> strategy;
    private AttackModifier attackModifier = new AttackModifier();
    private List<CombatListener<? super T>> listeners = new LinkedList<>();
    private CombatEventManager<T> eventManager = new CombatEventManager<>();

    /** The cache of damage dealt to this controller during combat. */
    private final CombatDamage damageCache = new CombatDamage();

    private List<CombatListener<? super T>> pendingAddition = new LinkedList<>();
    private List<CombatListener<? super T>> pendingRemoval = new LinkedList<>();
    private int[] delays = new int[3];

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
        for(Actor actor : World.get().getActors()) {
            if (actor == null || !actor.getState().equals(EntityState.ACTIVE)) continue;
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

        eventManager.sequence();
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

        listeners.forEach(attack -> attack.getModifier(attacker).ifPresent(this::addModifier));
        strategy.getModifier(attacker).ifPresent(this::addModifier);

        CombatHit[] hits = strategy.getHits(attacker, defender);
        submitHits(defender, strategy, hits);

        int delayType = strategy.getCombatType().ordinal();
        setDelay(delayType, strategy.getAttackDelay(attacker, type));
        return true;
    }

    private int hitsQueued;

    private void submitHits(Actor defender, CombatStrategy<? super T> strategy, CombatHit... hits) {
        CombatEvent<T> startEvent = new StartEvent<>(attacker, defender, listeners, strategy);
        CombatEvent<T> finishEvent = new FinishEvent<>(attacker, defender, listeners, strategy);

        eventManager.add(startEvent);

        for (CombatHit hit : hits) {
            CombatChain<T> chain = CombatChain.create();
            hitsQueued++;

            chain.link(new AttackEvent<>(attacker, defender, listeners, strategy, hit));
            chain.link(new HitEvent<>(attacker, defender, listeners, strategy, hit));

            int hsDelay = hitsQueued > 2 ? 2 * ((hitsQueued - 3) / 2 + 1) - 1 : 0;
            hsDelay += hit.getHitsplatDelay();

            chain.link(new HitsplatEvent<>(attacker, defender, listeners, strategy, hit, hsDelay));

            eventManager.add(chain.link(finishEvent));
        }

        eventManager.add(finishEvent);
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

    public void block(Actor attacker, Hit hit, CombatType combatType) {
        T defender = this.attacker;
        lastBlocked.reset();
        lastAttacker = attacker;
        listeners.forEach(attack -> attack.block(attacker, defender, hit, combatType));
    }

    public void onDeath(Actor attacker, Hit hit) {
        T defender = this.attacker;
        listeners.forEach(attack -> attack.onDeath(attacker, defender, hit));
        defender.getMovementQueue().reset();
    }

    public void decrementQueuedHits() {
        new Task(3) {
            @Override
            protected void execute() {
                hitsQueued--;
                cancel();
            }
        }.submit();
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

    public void setLastDefender(Actor lastDefender) {
        this.lastDefender = lastDefender;
        lastAttacked.reset();
    }

    private static boolean stopwatchElapsed(Stopwatch stopwatch, int seconds) {
        return stopwatch.elapsed(seconds, TimeUnit.SECONDS);
    }

}
