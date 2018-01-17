package net.edge.world.entity.actor.combat.strategy.npc;

import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.combat.CombatType;
import net.edge.world.entity.actor.combat.attack.FightType;
import net.edge.world.entity.actor.combat.hit.CombatHit;
import net.edge.world.entity.actor.combat.hit.Hit;
import net.edge.world.entity.actor.combat.strategy.CombatStrategy;
import net.edge.world.entity.actor.mob.Mob;

public abstract class MultiStrategy extends CombatStrategy<Mob> {

    protected CombatStrategy<Mob> currentStrategy;

    @Override
    public boolean withinDistance(Mob attacker, Actor defender) {
        return currentStrategy.withinDistance(attacker, defender);
    }

    @Override
    public boolean canAttack(Mob attacker, Actor defender) {
        return currentStrategy.canAttack(attacker, defender);
    }

    @Override
    public boolean canOtherAttack(Actor attacker, Mob defender) {
        return currentStrategy.canOtherAttack(attacker, defender);
    }

    @Override
    public void start(Mob attacker, Actor defender, Hit[] hits) {
        currentStrategy.start(attacker, defender, hits);
    }

    @Override
    public void attack(Mob attacker, Actor defender, Hit hit) {
        currentStrategy.attack(attacker, defender, hit);
    }

    @Override
    public void hit(Mob attacker, Actor defender, Hit hit) {
        currentStrategy.hit(attacker, defender, hit);
    }

    @Override
    public void hitsplat(Mob attacker, Actor defender, Hit hit) {
        currentStrategy.hitsplat(attacker, defender, hit);
    }

    @Override
    public void block(Actor attacker, Mob defender, Hit hit, CombatType combatType) {
        currentStrategy.block(attacker, defender, hit, combatType);
    }

    @Override
    public void preDeath(Actor attacker, Mob defender, Hit hit) {
        currentStrategy.preDeath(attacker, defender, hit);
    }

    @Override
    public void onDeath(Actor attacker, Mob defender, Hit hit) {
        currentStrategy.onDeath(attacker, defender, hit);
    }

    @Override
    public void preKill(Mob attacker, Actor defender, Hit hit) {
        currentStrategy.preKill(attacker, defender, hit);
    }

    @Override
    public void onKill(Mob attacker, Actor defender, Hit hit) {
        currentStrategy.onKill(attacker, defender, hit);
    }

    @Override
    public void finishIncoming(Actor attacker, Mob defender) {
        currentStrategy.finishIncoming(attacker, defender);
    }

    @Override
    public void finishOutgoing(Mob attacker, Actor defender) {
        currentStrategy.finishOutgoing(attacker, defender);
    }

    @Override
    public int getAttackDelay(Mob attacker, Actor defender, FightType fightType) {
        return currentStrategy.getAttackDelay(attacker, defender, fightType);
    }

    @Override
    public int getAttackDistance(Mob attacker, FightType fightType) {
        return currentStrategy.getAttackDistance(attacker, fightType);
    }

    @Override
    public CombatHit[] getHits(Mob attacker, Actor defender) {
        return currentStrategy.getHits(attacker, defender);
    }

    @Override
    public Animation getAttackAnimation(Mob attacker, Actor defender) {
        return currentStrategy.getAttackAnimation(attacker, defender);
    }

    @Override
    public CombatType getCombatType() {
        return currentStrategy.getCombatType();
    }

    @Override
    public int modifyAccuracy(Mob attacker, Actor defender, int roll) {
        return currentStrategy.modifyAccuracy(attacker, defender, roll);
    }

    @Override
    public int modifyAggressive(Mob attacker, Actor defender, int roll) {
        return currentStrategy.modifyAggressive(attacker, defender, roll);
    }

    @Override
    public int modifyDefensive(Actor attacker, Mob defender, int roll) {
        return currentStrategy.modifyDefensive(attacker, defender, roll);
    }

    @Override
    public int modifyDamage(Mob attacker, Actor defender, int damage) {
        return currentStrategy.modifyDamage(attacker, defender, damage);
    }

    @Override
    public int modifyAttackLevel(Mob attacker, Actor defender, int level) {
        return currentStrategy.modifyAttackLevel(attacker, defender, level);
    }

    @Override
    public int modifyStrengthLevel(Mob attacker, Actor defender, int level) {
        return currentStrategy.modifyStrengthLevel(attacker, defender, level);
    }

    @Override
    public int modifyDefenceLevel(Actor attacker, Mob defender, int level) {
        return currentStrategy.modifyDefenceLevel(attacker, defender, level);
    }

    @Override
    public int modifyRangedLevel(Mob attacker, Actor defender, int level) {
        return currentStrategy.modifyRangedLevel(attacker, defender, level);
    }

    @Override
    public int modifyMagicLevel(Mob attacker, Actor defender, int level) {
        return currentStrategy.modifyMagicLevel(attacker, defender, level);
    }

    @Override
    public int modifyOffensiveBonus(Mob attacker, Actor defender, int bonus) {
        return currentStrategy.modifyOffensiveBonus(attacker, defender, bonus);
    }

    @Override
    public int modifyAggressiveBonus(Mob attacker, Actor defender, int bonus) {
        return currentStrategy.modifyAggressiveBonus(attacker, defender, bonus);
    }

    @Override
    public int modifyDefensiveBonus(Actor attacker, Mob defender, int bonus) {
        return currentStrategy.modifyDefensiveBonus(attacker, defender, bonus);
    }

    protected static CombatStrategy<Mob> randomStrategy(CombatStrategy<Mob>[] strategies) {
        return RandomUtils.random(strategies);
    }

    @SafeVarargs
    protected static CombatStrategy<Mob>[] createStrategyArray(CombatStrategy<Mob>... strategies) {
        return strategies;
    }

}
