package net.edge.content.newcombat.strategy;

import net.edge.content.newcombat.attack.AttackStance;
import net.edge.content.newcombat.formula.FormulaFactory;
import net.edge.content.newcombat.hit.CombatHit;
import net.edge.content.newcombat.hit.Hit;
import net.edge.world.entity.actor.Actor;

public abstract class CombatStrategy<T extends Actor> implements CombatAttack<T> {

    public abstract boolean withinDistance(T attacker, Actor defender);

    public abstract boolean canAttack(T attacker, Actor defender);

    public abstract int getAttackDelay(AttackStance stance);

    public abstract int getAttackDistance(AttackStance stance);

    public abstract CombatHit[] getHits(T attacker, Actor defender);

    @Override
    public void attack(T attacker, Actor defender, Hit hit, Hit[] hits) {
    }

    @Override
    public void hit(T attacker, Actor defender, Hit hit, Hit[] hits) {
    }

    @Override
    public void hitsplat(T attacker, Actor defender, Hit hit, Hit[] hits) {
    }

    @Override
    public void block(Actor attacker, T defender, Hit hit, Hit[] hits) {
    }

    @Override
    public void onDeath(Actor attacker, T defender, Hit hit, Hit[] hits) {
        /*
        let me show you have I have so far
         */
    }

    @Override
    public void finish(T attacker, Actor defender, Hit[] hits) {
    }

    protected final CombatHit nextMeleeHit(T attacker, Actor defender) {
        return nextMeleeHit(attacker, defender, 0, 1);
    }

    protected final CombatHit nextRangedHit(T attacker, Actor defender) {
        return nextRangedHit(attacker, defender, 2, 1);
    }

    protected final CombatHit nextMagicHit(T attacker, Actor defender, int max) {
        return nextMagicHit(attacker, defender, max, 2, 1);
    }

    protected final CombatHit nextMeleeHit(T attacker, Actor defender, int hitDelay, int hitsplatDelay) {
        return new CombatHit(FormulaFactory.nextMeleeHit(attacker, defender), hitDelay, hitsplatDelay);
    }

    protected final CombatHit nextRangedHit(T attacker, Actor defender, int hitDelay, int hitsplatDelay) {
        return new CombatHit(FormulaFactory.nextRangedHit(attacker, defender), hitDelay, hitsplatDelay);
    }

    protected final CombatHit nextMagicHit(T attacker, Actor defender, int max, int hitDelay, int hitsplatDelay) {
        return new CombatHit(FormulaFactory.nextMagicHit(attacker, defender, max), hitDelay, hitsplatDelay);
    }

    protected static boolean isPoisonous(String name) {
        return name.endsWith("p)") || name.endsWith("p+)") || name.endsWith("p++)");
    }

    protected static int poisonStrength(String name) {
        return name.endsWith("p)") ? 2 : name.endsWith("p+)") ? 4 : name.endsWith("p++)") ? 6 : 2;
    }

}
