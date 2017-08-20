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
import net.edge.world.World;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.update.UpdateFlag;

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

    private Poison poison;
    private FightType type;

    private CombatStrategy<? super T> strategy;
    private AttackModifier attackModifier = new AttackModifier();
    private List<CombatAttack<? super T>> attacks = new LinkedList<>();
    private CombatEventManager eventManager = new CombatEventManager();

    /** The cache of damage dealt to this controller during combat. */
    private final CombatDamage damageCache = new CombatDamage();

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

    public static void update() {
        for(Actor a : World.get().getActors()) {
            if (a == null || !a.getState().equals(EntityState.ACTIVE)) continue;
            a.getCombat().tick();

            if (!a.getHitQueue().isEmpty()) {
                a.getFlags().flag(UpdateFlag.PRIMARY_HIT);
                if (a.getHitQueue().size() > 1) {
                    a.getFlags().flag(UpdateFlag.SECONDARY_HIT);
                }
            }
        }
    }

    private void tick() {
//        try {
            handleListeners();
            if (poison != null) poison.sequence(attacker);
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
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void handleListeners() {
        if (!pendingAddition.isEmpty()) {
            pendingAddition.forEach(attack -> {
                attacks.add(attack);
                attack.getModifier(attacker, defender)
                        .ifPresent(this::addModifier);
            });
            pendingAddition.clear();
        }

        if (!pendingRemoval.isEmpty()) {
            pendingRemoval.forEach(attack -> {
                attacks.remove(attack);
                attack.getModifier(attacker, defender)
                        .ifPresent(this::removeModifier);
            });
            pendingRemoval.clear();
        }
    }

    public boolean submitStrategy(Actor defender, CombatStrategy<? super T> strategy) {
        if (!canAttack(defender)) {
            return false;
        }

        if (!strategy.withinDistance(attacker, defender)) {
            return false;
        }

        if (!strategy.canAttack(attacker, defender)) {
            return false;
        }

        strategy.getModifier(attacker, defender).ifPresent(this::addModifier);
        CombatHit[] hits = strategy.getHits(attacker, defender);
        strategy.getModifier(attacker, defender).ifPresent(this::removeModifier);
        submitHits(defender, strategy, hits);
        int delayType = strategy.getCombatType().ordinal();
        setDelay(delayType, strategy.getAttackDelay(attacker, type));
        return true;
    }

    private void submitHits(Actor defender, CombatStrategy<? super T> strategy, CombatHit... hits) {
        int shortest = Integer.MAX_VALUE;
        for (CombatHit hit : hits) {
            int delay = 0;
            eventManager.add(new CombatEvent(defender, delay, hit, (def, _hit) -> {
                if (!canAttack(def)) {
                    reset();
                    eventManager.cancel(defender);
                    return;
                }
                attack(def, _hit);
                strategy.attack(attacker, def, _hit);
            }));

            delay += hit.getHitDelay();
            eventManager.add(new CombatEvent(defender, delay, hit, (def, _hit) -> {
                if (!canAttack(def)) {
                    reset();
                    eventManager.cancel(defender);
                    return;
                }
                hit(def, _hit);
                strategy.hit(attacker, def, _hit);
            }));

            delay += hit.getHitsplatDelay();
            eventManager.add(new CombatEvent(defender, delay, hit, (def, _hit) -> {
                if (!canAttack(def)) {
                    reset();
                    eventManager.cancel(defender);
                    return;
                }
                hitsplat(def, _hit, strategy.getCombatType());
                strategy.hitsplat(attacker, def, _hit);
            }));

            if (shortest > delay) shortest = delay;
        }
        eventManager.add(new CombatEvent(defender, shortest, (def, _hit) -> {
            if (!canAttack(def)) {
                reset();
                eventManager.cancel(defender);
                return;
            }
            finish(def);
            strategy.finish(attacker, def);
        }));
    }

    public void submitHits(Actor defender, CombatHit... hits) {
        submitHits(defender, strategy, hits);
    }

    public boolean canAttack(Actor defender) {
        return validate(attacker) && validate(defender) && attacker.getInstance() == defender.getInstance();

    }

    private void attack(Actor defender, CombatHit hit) {
        lastDefender = defender;
        lastAttacked.reset();
        attacks.forEach(attack -> attack.attack(attacker, defender, hit));
    }

    private void hit(Actor defender, Hit hit) {
        attacks.forEach(attack -> attack.hit(attacker, defender, hit));

//        if (defender.isAutoRetaliate()) {
//            defender.getCombat().attack(attacker);
//        }
    }

    private void hitsplat(Actor defender, Hit hit, CombatType combatType) {
        attacks.forEach(attack -> attack.hitsplat(attacker, defender, hit));
        defender.getCombat().block(attacker, hit, combatType);
        defender.getCombat().damageCache.add(attacker, hit.getDamage());

        if (combatType != CombatType.MAGIC || defender.isMob()) {
            defender.animation(CombatUtil.getBlockAnimation(defender));
        }

        if (combatType != CombatType.MAGIC || hit.isAccurate()) {
            defender.damage(hit);

            if (defender.getCurrentHealth() <= 0) {
                defender.getCombat().onDeath(attacker, hit);
                defender.getCombat().reset();
                reset();
            }
        }
    }

    private void block(Actor attacker, Hit hit, CombatType combatType) {
        T defender = this.attacker;
        lastBlocked.reset();
        lastAttacker = attacker;
        attacks.forEach(attack -> attack.block(attacker, defender, hit, combatType));
        defender.getMovementQueue().reset();
    }

    private void onDeath(Actor attacker, Hit hit) {
        T defender = this.attacker;
        attacks.forEach(attack -> attack.onDeath(attacker, defender, hit));
        defender.getMovementQueue().reset();

        if (!defender.getHitQueue().isEmpty()) {
            defender.getHitQueue().clear();
        }
    }

    private void finish(Actor defender) {
        attacks.forEach(attack -> attack.finish(attacker, defender));
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

    private void addListener(CombatAttack<? super T> attack) {
        pendingAddition.add(attack);
    }

    private void removeListener(CombatAttack<? super T> attack) {
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

    public Actor getLastAttacker() {
        return lastAttacker;
    }

    public Actor getLastDefender() {
        return lastDefender;
    }

    public CombatStrategy<? super T> getStrategy() {
        return strategy;
    }

    private static boolean stopwatchElapsed(Stopwatch stopwatch, int seconds) {
        return stopwatch.elapsed(seconds, TimeUnit.SECONDS);
    }

    private static boolean validate(Actor actor) {
        return actor != null && !actor.isDead() && actor.isVisible() && !actor.isTeleporting() && !actor.isNeedsPlacement();
    }

    private void setDelay(int index, int delay) {
        for (int idx = 0; idx < delays.length; idx++) {
            if (idx != index) {
                delays[idx] += 2;
            } else {
                if (delays[idx] < delay)
                delays[idx] = delay;
            }
        }
    }

}
