package net.edge.content.newcombat;

import net.edge.content.combat.weapon.FightType;
import net.edge.content.newcombat.attack.AttackModifier;
import net.edge.content.newcombat.content.Poison;
import net.edge.content.newcombat.events.CombatEvent;
import net.edge.content.newcombat.events.CombatEventManager;
import net.edge.content.newcombat.hit.CombatHit;
import net.edge.content.newcombat.hit.Hit;
import net.edge.content.newcombat.strategy.CombatAttack;
import net.edge.content.newcombat.strategy.CombatStrategy;
import net.edge.world.entity.actor.Actor;

import java.util.LinkedList;
import java.util.List;

public class Combat<T extends Actor> {
    private final T attacker;
    private   Actor defender;

    private FightType type;

    private int lastAttacked;
    private int lastBlocked;

    private Poison poison;

    private CombatStrategy<? super T> strategy;
    private AttackModifier attackModifier = new AttackModifier();
    private List<CombatAttack<? super T>> attacks = new LinkedList<>();
    private CombatEventManager eventManager = new CombatEventManager();

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
                    pendingAddition.forEach(attacks::add);
                    pendingAddition.clear();
                }

                if (!pendingRemoval.isEmpty()) {
                    pendingRemoval.forEach(attacks::remove);
                    pendingRemoval.clear();
                }

                if (strategy != null) {
                    if (delays[strategy.getCombatType().ordinal()] > 0) {
                        delays[strategy.getCombatType().ordinal()]--;
                    } else if (defender != null) {
                        submitStrategy(defender, strategy);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void submitStrategy(Actor defender, CombatStrategy<? super T> strategy) {
        if (!strategy.withinDistance(attacker, defender)) {
            attacker.getMovementQueue().follow(defender);
            return;
        }

        if (!strategy.canAttack(attacker, defender)) {
            reset();
            return;
        }

        CombatHit[] hits = strategy.getHits(attacker, defender);
        delays[strategy.getCombatType().ordinal()] = strategy.getAttackDelay(type);
        submitHits(defender, hits);
    }

    public void submitHits(Actor defender, CombatHit... hits) {
        isHitActive = true;
        int shortest = Integer.MAX_VALUE;
        for (CombatHit hit : hits) {
            int delay = 0;
            eventManager.add(new CombatEvent(defender, delay, hit, hits, this::attack));

            delay += hit.getHitDelay();
            eventManager.add(new CombatEvent(defender, delay, hit, hits, this::hit));

            delay += hit.getHitsplatDelay();
            eventManager.add(new CombatEvent(defender, delay, hit, hits, this::hitsplat));

            if (shortest > delay) shortest = delay;
        }
        eventManager.add(new CombatEvent(defender, shortest, hits, (def, hit, hits2) -> {
            finish(def, hits2);
            isHitActive = false;
        }));
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
        attacks.forEach(attack -> attack.attack(attacker, defender, hit, hits));
    }

    private void hit(Actor defender, Hit hit, Hit[] hits) {
        attacks.forEach(attack -> attack.hit(attacker, defender, hit, hits));
    }

    private void hitsplat(Actor defender, Hit hit, Hit[] hits) {
        attacks.forEach(attack -> attack.hitsplat(attacker, defender, hit, hits));
        defender.getNewCombat().block(attacker, hit, hits);

        net.edge.world.Hit other = new net.edge.world.Hit(10, net.edge.world.Hit.HitType.NORMAL, net.edge.world.Hit.HitIcon.DEFLECT);
        defender.damage(other);

        if (defender.getCurrentHealth() <= 0) {
            defender.getNewCombat().onDeath(attacker, hit, hits);
            reset();
        }
    }

    private void block(Actor attacker, Hit hit, Hit[] hits) {
        T defender = this.attacker;
        attacks.forEach(attack -> attack.block(attacker, defender, hit, hits));
        defender.getMovementQueue().reset();
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
        if (strategy != next) {
            if (strategy != null) {
                removeListener(strategy);
                strategy.getModifier(attacker, defender).ifPresent(this::removeModifier);
            }

            if (next != null) {
                addListener(next);
                next.getModifier(attacker, defender).ifPresent(this::addModifier);
            }

            reset();
            strategy = next;
        }
    }

    public Actor getDefender() {
        return defender;
    }

}
