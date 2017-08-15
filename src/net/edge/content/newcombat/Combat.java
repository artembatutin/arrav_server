package net.edge.content.newcombat;

import net.edge.content.newcombat.attack.AttackModifier;
import net.edge.content.newcombat.attack.AttackStance;
import net.edge.content.newcombat.attack.AttackStyle;
import net.edge.content.newcombat.events.CombatEvent;
import net.edge.content.newcombat.events.CombatEventManager;
import net.edge.content.newcombat.hit.CombatHit;
import net.edge.content.newcombat.hit.Hit;
import net.edge.content.newcombat.strategy.CombatAttack;
import net.edge.content.newcombat.strategy.CombatStrategy;
import net.edge.content.newcombat.content.Poison;
import net.edge.world.entity.actor.Actor;

import java.util.LinkedList;
import java.util.List;

public class Combat<T extends Actor> {
    private final T attacker;
    private   Actor defender;

    private AttackStance stance;
    private AttackStyle style;

    private int lastAttacked;
    private int lastBlocked;

    private Poison poison;

    private int attackDelay = 0;
    private CombatStrategy<? super T> strategy;
    private AttackModifier attackModifier = new AttackModifier();
    private List<CombatAttack<? super T>> attacks = new LinkedList<>();
    private CombatEventManager eventManager = new CombatEventManager();

    public Combat(T attacker) {
        this.attacker = attacker;
        style = AttackStyle.CRUSH;
        stance = AttackStance.ACCURATE;
    }

    public void attack(Actor defender) {
        this.defender = defender;
        attacker.getMovementQueue().follow(defender);
    }

    public void tick() {
        execute();
        eventManager.sequence();
        poison.sequence(attacker);
    }

    private void execute() {
        if (attackDelay <= 0) {
            if (strategy != null) {
                submitStrategy(defender, strategy);
            }
        } else {
            attackDelay--;
        }
    }

    public void submitStrategy(Actor defender, CombatStrategy<? super T> strategy) {
        CombatHit[] hits = strategy.getHits(attacker, defender);

        int shortest = 0;
        for (CombatHit hit : hits) {
            int delay = hit.getHitDelay() + hit.getHitsplatDelay();
            submitHit(defender, hit);
            if (delay < shortest) shortest = delay;
        }

        eventManager.add(new CombatEvent(defender, shortest, hits, this::finish));
        attackDelay = strategy.getAttackDelay(stance);
    }

    public void submitHit(Actor defender, CombatHit hit) {
        CombatHit[] hits = new CombatHit[] { hit };

        int delay = 0;
        eventManager.add(new CombatEvent(defender, delay, hit, hits, this::attack));

        delay += hit.getHitDelay();
        eventManager.add(new CombatEvent(defender, delay, hit, hits, this::hit));

        delay += hit.getHitsplatDelay();
        eventManager.add(new CombatEvent(defender, delay, hit, hits, this::hitsplat));
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

    public void attack(Actor defender, CombatHit hit, CombatHit[] hits) {
        attacks.forEach(attack -> attack.attack(attacker, defender, hit, hits));
    }

    public void hit(Actor defender, Hit hit, Hit[] hits) {
        attacks.forEach(attack -> attack.hit(attacker, defender, hit, hits));
        defender.getNewCombat().block(defender, hit, hits);

        net.edge.world.Hit other = new net.edge.world.Hit(hit.getDamage(), net.edge.world.Hit.HitType.NORMAL, net.edge.world.Hit.HitIcon.DEFLECT);
        defender.damage(other);

        if (defender.getCurrentHealth() <= 0) {
            defender.getNewCombat().onDeath(attacker, hit, hits);
            defender.getNewCombat().reset();
            reset();
        }
    }

    private void hitsplat(Actor defender, Hit hit, Hit[] hits) {
        attacks.forEach(attack -> attack.hitsplat(attacker, defender, hit, hits));
    }

    public void block(Actor attacker, Hit hit, Hit[] hits) {
        T defender = this.attacker;
        attacks.forEach(attack -> attack.block(attacker, defender, hit, hits));
    }

    private void onDeath(Actor attacker, Hit hit, Hit[] hits) {
        T defender = this.attacker;
        attacks.forEach(attack -> attack.onDeath(attacker, defender, hit, hits));
    }

    private void finish(Actor defender, Hit hit, Hit[] hits) {
        attacks.forEach(attack -> attack.finish(attacker, defender, hits));
    }

    private void addModifier(AttackModifier modifier) {
        attackModifier.add(modifier);
    }

    private void removeModifier(AttackModifier modifier) {
        attackModifier.remove(modifier);
    }

    private void addListener(CombatAttack<? super T> attack) {
        attacks.add(attack);
    }

    private void removeListener(CombatAttack<? super T> attack) {
        attacks.add(attack);
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

    public AttackStance getAttackStance() {
        return stance;
    }

    public void setAttackStance(AttackStance stance) {
        this.stance = stance;
    }

    public AttackStyle getAttackStyle() {
        return style;
    }

    public void setAttackStyle(AttackStyle style) {
        this.style = style;
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
