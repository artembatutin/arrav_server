package net.edge.content.combat;

import net.edge.content.combat.attack.AttackModifier;
import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.content.Poison;
import net.edge.content.combat.events.CombatEvent;
import net.edge.content.combat.events.CombatEventManager;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.strategy.CombatAttack;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.util.Stopwatch;
import net.edge.world.entity.actor.Actor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Combat<T extends Actor> {
    private final T attacker;
    private   Actor defender;

    private Actor lastAttacker;
    private Actor lastDefender;

    private FightType type;

    private Stopwatch lastAttacked = new Stopwatch();
    private Stopwatch lastBlocked = new Stopwatch();

    private Poison poison;

    private CombatStrategy<? super T> strategy;
    private AttackModifier attackModifier = new AttackModifier();
    private List<CombatAttack<? super T>> attacks = new LinkedList<>();
    private CombatEventManager eventManager = new CombatEventManager();

    /**
     * The cache of damage dealt to this controller during combat.
     */
    private final CombatDamage damageCache = new CombatDamage();

    private boolean isHitActive;
    private List<CombatAttack<? super T>> pendingAddition = new LinkedList<>();
    private List<CombatAttack<? super T>> pendingRemoval = new LinkedList<>();
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

    public void tick() {
        try {
            eventManager.sequence();

            if (poison != null) {
                poison.sequence(attacker);
            }

            if (!isHitActive) {
                if (!pendingAddition.isEmpty()) {
                    pendingAddition.forEach(attack -> {
                        attacks.add(attack);
                        attack.getModifier(attacker, defender).ifPresent(this::addModifier);
                    });
                    pendingAddition.clear();
                }

                if (!pendingRemoval.isEmpty()) {
                    pendingRemoval.forEach(attack -> {
                        attacks.remove(attack);
                        attack.getModifier(attacker, defender).ifPresent(this::removeModifier);
                    });
                    pendingRemoval.clear();
                }
            }

            for (int next = 0; next < delays.length; next++) {
                if (delays[next] > 0) {
                    delays[next]--;
                } else if (strategy != null && defender != null && strategy.getCombatType().ordinal() == next) {
                    submitStrategy(defender, strategy);
                    delays[strategy.getCombatType().ordinal()] = strategy.getAttackDelay(attacker, defender, type);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void submitStrategy(Actor defender, CombatStrategy<? super T> strategy) {
        if (!strategy.withinDistance(attacker, defender)) {
            return;
        }

        if (!strategy.canAttack(attacker, defender)) {
            return;
        }

        strategy.getModifier(attacker, defender).ifPresent(this::addModifier);
        CombatHit[] hits = strategy.getHits(attacker, defender);
        strategy.getModifier(attacker, defender).ifPresent(this::removeModifier);
        submitHits(defender, strategy, hits);
    }

    private void submitHits(Actor defender, CombatStrategy<? super T> strategy, CombatHit... hits) {
        int shortest = Integer.MAX_VALUE;

        for (CombatHit hit : hits) {
            int delay = 0;
            eventManager.add(new CombatEvent(defender, delay, hit, hits, (def, _hit, _hits) -> {
                isHitActive = true;
                attack(def, _hit, _hits);
                strategy.attack(attacker, def, _hit, _hits);
            }));

            delay += hit.getHitDelay();
            eventManager.add(new CombatEvent(defender, delay, hit, hits, (def, _hit, _hits) -> {
                hit(def, _hit, _hits);
                strategy.hit(attacker, def, _hit, _hits);
            }));

            delay += hit.getHitsplatDelay();
            eventManager.add(new CombatEvent(defender, delay, hit, hits, (def, _hit, _hits) -> {
                hitsplat(def, _hit, _hits);
                strategy.hitsplat(attacker, def, _hit, _hits);
            }));

            if (shortest > delay) shortest = delay;
        }
        eventManager.add(new CombatEvent(defender, shortest, hits, (def, _hit, _hits) -> {
            finish(def, _hits);
            strategy.finish(attacker, def, _hits);
            isHitActive = false;
        }));
    }

    public void submitHits(Actor defender, CombatHit... hits) {
        submitHits(defender, strategy, hits);
    }

    public void poison(int strength) {
        if (strength <= 0) {
            poison = null;
        } else {
            Poison next = new Poison(strength);

            if (poison != null && poison.replace(next)) {
                poison = next;
            }
        }
    }

    public void reset() {
        defender = null;
        attacker.getMovementQueue().reset();
    }

    private void attack(Actor defender, CombatHit hit, CombatHit[] hits) {
        lastAttacked.reset();
        lastDefender = defender;
        attacks.forEach(attack -> attack.attack(attacker, defender, hit, hits));
    }

    private void hit(Actor defender, Hit hit, Hit[] hits) {
        attacks.forEach(attack -> attack.hit(attacker, defender, hit, hits));
    }

    private void hitsplat(Actor defender, Hit hit, Hit[] hits) {
        attacks.forEach(attack -> attack.hitsplat(attacker, defender, hit, hits));
        defender.getNewCombat().block(attacker, hit, hits);
        defender.damage(hit);

        if (defender.getCurrentHealth() <= 0) {
            defender.getNewCombat().onDeath(attacker, hit, hits);
            reset();
        }
    }

    private void block(Actor attacker, Hit hit, Hit[] hits) {
        T defender = this.attacker;
        lastBlocked.reset();
        lastAttacker = attacker;
        defender.getMovementQueue().reset();
        attacks.forEach(attack -> attack.block(attacker, defender, hit, hits));
    }

    private void onDeath(Actor attacker, Hit hit, Hit[] hits) {
        T defender = this.attacker;
        attacks.forEach(attack -> attack.onDeath(attacker, defender, hit, hits));
        defender.getMovementQueue().reset();
    }

    private void finish(Actor defender, Hit[] hits) {
        attacks.forEach(attack -> attack.finish(attacker, defender, hits));
    }

    public void addModifier(AttackModifier modifier) {
        attackModifier.add(modifier);
    }

    public void removeModifier(AttackModifier modifier) {
        attackModifier.remove(modifier);
    }

    private void addListener(CombatAttack<? super T> attack) {
        pendingAddition.add(attack);
    }

    private void removeListener(CombatAttack<? super T> attack) {
        pendingRemoval.add(attack);
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

    public Poison getPoison() {
        return poison;
    }

    public void setPoison(Poison poison) {
        this.poison = poison;
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

    public boolean inCombat() {
        return isAttacking() || isUnderAttack();
    }

    public boolean isAttacking() {
        return !stopwatchElapsed(lastAttacked, CombatConstants.COMBAT_TIMER) && lastDefender != null;
    }

    public boolean isUnderAttack() {
        return !stopwatchElapsed(lastBlocked, CombatConstants.COMBAT_TIMER) && lastAttacker != null;
    }

    public boolean isAttacking(Actor defender) {
        return !stopwatchElapsed(lastAttacked, CombatConstants.COMBAT_TIMER) && defender != null && lastDefender == defender;
    }

    public boolean isUnderAttackBy(Actor attacker) {
        return !stopwatchElapsed(lastBlocked, CombatConstants.COMBAT_TIMER) && attacker != null && lastAttacker == attacker;
    }

    private static boolean stopwatchElapsed(Stopwatch stopwatch, int seconds) {
        return stopwatch.elapsed(seconds, TimeUnit.SECONDS);
    }

    public Actor getLastAttacker() {
        return lastAttacker;
    }

    public Actor getLastDefender() {
        return lastDefender;
    }

    public CombatStrategy<? super T> getStrategy() {
        return strategy;
    }
}
