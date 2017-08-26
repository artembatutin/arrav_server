package net.edge.content.combat.strategy.npc;

import net.edge.content.combat.CombatType;
import net.edge.content.combat.attack.AttackModifier;
import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;

import java.util.Optional;

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
    public void onDeath(Actor attacker, Mob defender, Hit hit) {
        currentStrategy.onDeath(attacker, defender, hit);
    }

    @Override
    public void finish(Mob attacker, Actor defender) {
        currentStrategy.finish(attacker, defender);
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
    public Optional<AttackModifier> getModifier(Mob attacker) {
        return currentStrategy.getModifier(attacker);
    }

    protected static CombatStrategy<Mob> randomStrategy(CombatStrategy<Mob>[] strategies) {
        return RandomUtils.random(strategies);
    }

    @SafeVarargs
    protected static CombatStrategy<Mob>[] createStrategyArray(CombatStrategy<Mob>... strategies) {
        return strategies;
    }

}
