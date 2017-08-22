package net.edge.content.combat.strategy;

import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.CombatType;
import net.edge.content.combat.attack.FormulaFactory;
import net.edge.content.combat.attack.listener.CombatListener;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.hit.Hit;
import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;

public abstract class CombatStrategy<T extends Actor> implements CombatListener<T> {

    public abstract boolean withinDistance(T attacker, Actor defender);

    public abstract boolean canAttack(T attacker, Actor defender);

    public abstract int getAttackDelay(T attacker, FightType fightType);

    public abstract int getAttackDistance(T attacker, FightType fightType);

    public abstract CombatHit[] getHits(T attacker, Actor defender);

    public abstract Animation getAttackAnimation(T attacker, Actor defender);

    @Override
    public void start(T attacker, Actor defender) {
    }

    @Override
    public void attack(T attacker, Actor defender, Hit hit) {
    }

    @Override
    public void hit(T attacker, Actor defender, Hit hit) {
    }

    @Override
    public void hitsplat(T attacker, Actor defender, Hit hit) {
    }

    @Override
    public void block(Actor attacker, T defender, Hit hit, CombatType combatType) {
    }

    @Override
    public void onDeath(Actor attacker, T defender, Hit hit) {
    }

    @Override
    public void finish(T attacker, Actor defender) {
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

    public abstract CombatType getCombatType();
}
