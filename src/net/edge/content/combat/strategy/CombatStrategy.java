package net.edge.content.combat.strategy;

import net.edge.content.combat.CombatUtil;
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

    public abstract int getAttackDelay(T attacker, Actor defender, FightType fightType);

    public abstract int getAttackDistance(T attacker, FightType fightType);

    public abstract CombatHit[] getHits(T attacker, Actor defender);

    public abstract Animation getAttackAnimation(T attacker, Actor defender);

    @Override
    public void start(T attacker, Actor defender, Hit[] hits) {
    }

    @Override
    public void attack(T attacker, Actor defender, Hit hit) {
    }

    @Override
    public void hit(T attacker, Actor defender, Hit hit) {
    }

    @Override
    public final void hitsplat(T attacker, Actor defender, Hit hit) {
    }

    @Override
    public void block(Actor attacker, T defender, Hit hit, CombatType combatType) {
        throw new UnsupportedOperationException("Combat strategies can't define a block method!");
    }

    @Override
    public void onDeath(Actor attacker, T defender, Hit hit) {
    }

    @Override
    public void finish(T attacker, Actor defender) {
    }

    public abstract CombatType getCombatType();

    protected final CombatHit nextMeleeHit(T attacker, Actor defender) {
        int hitDelay = CombatUtil.getHitDelay(attacker, defender, getCombatType());
        int hitsplatDelay = CombatUtil.getHitsplatDelay(getCombatType());
        return nextMeleeHit(attacker, defender, hitDelay, hitsplatDelay);
    }

    protected final CombatHit nextMeleeHit(T attacker, Actor defender, int maxHit) {
        int hitDelay = CombatUtil.getHitDelay(attacker, defender, getCombatType());
        int hitsplatDelay = CombatUtil.getHitsplatDelay(getCombatType());
        return nextMeleeHit(attacker, defender, maxHit, hitDelay, hitsplatDelay);
    }

    protected final CombatHit nextRangedHit(T attacker, Actor defender) {
        int hitDelay = CombatUtil.getHitDelay(attacker, defender, getCombatType());
        int hitsplatDelay = CombatUtil.getHitsplatDelay(getCombatType());
        return nextRangedHit(attacker, defender, hitDelay, hitsplatDelay);
    }

    protected final CombatHit nextRangedHit(T attacker, Actor defender, int max) {
        int hitDelay = CombatUtil.getHitDelay(attacker, defender, getCombatType());
        int hitsplatDelay = CombatUtil.getHitsplatDelay(getCombatType());
        return nextRangedHit(attacker, defender, max, hitDelay, hitsplatDelay);
    }

    protected final CombatHit nextMagicHit(T attacker, Actor defender, int max) {
        int hitDelay = CombatUtil.getHitDelay(attacker, defender, getCombatType());
        int hitsplatDelay = CombatUtil.getHitsplatDelay(getCombatType());
        return nextMagicHit(attacker, defender, max, hitDelay, hitsplatDelay);
    }

    private CombatHit nextMeleeHit(T attacker, Actor defender, int maxHit, int hitDelay, int hitsplatDelay) {
        return new CombatHit(FormulaFactory.nextMeleeHit(attacker, defender, maxHit), hitDelay, hitsplatDelay);
    }

    private CombatHit nextMeleeHit(T attacker, Actor defender, int hitDelay, int hitsplatDelay) {
        return new CombatHit(FormulaFactory.nextMeleeHit(attacker, defender), hitDelay, hitsplatDelay);
    }

    private CombatHit nextRangedHit(T attacker, Actor defender, int hitDelay, int hitsplatDelay) {
        return new CombatHit(FormulaFactory.nextRangedHit(attacker, defender), hitDelay, hitsplatDelay);
    }

    private CombatHit nextRangedHit(T attacker, Actor defender, int max, int hitDelay, int hitsplatDelay) {
        return new CombatHit(FormulaFactory.nextRangedHit(attacker, defender, max), hitDelay, hitsplatDelay);
    }

    protected CombatHit nextMagicHit(T attacker, Actor defender, int max, int hitDelay, int hitsplatDelay) {
        return new CombatHit(FormulaFactory.nextMagicHit(attacker, defender, max), hitDelay, hitsplatDelay);
    }
}
