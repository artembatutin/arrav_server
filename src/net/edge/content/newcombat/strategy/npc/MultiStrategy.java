package net.edge.content.newcombat.strategy.npc;

import net.edge.content.newcombat.attack.AttackModifier;
import net.edge.content.newcombat.attack.AttackStance;
import net.edge.content.newcombat.hit.CombatHit;
import net.edge.content.newcombat.hit.Hit;
import net.edge.content.newcombat.strategy.CombatStrategy;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Handles npcs that use multiple strategies to attack.
 *
 * @author Michael | Chex
 */
public abstract class MultiStrategy extends CombatStrategy<Mob> {
    private final List<CombatStrategy<? super Mob>> strategies;
    protected CombatStrategy<? super Mob> currentStrategy;

    @SafeVarargs
    public MultiStrategy(CombatStrategy<? super Mob>... strategies) {
        this.strategies = new LinkedList<>(Arrays.asList(strategies));
        currentStrategy = getRandomStrategy();
    }

    @Override
    public boolean withinDistance(Mob attacker, Actor defender) {
        return currentStrategy.withinDistance(attacker, defender);
    }

    @Override
    public boolean canAttack(Mob attacker, Actor defender) {
        return currentStrategy.canAttack(attacker, defender);
    }

    @Override
    public void finish(Mob attacker, Actor defender, Hit[] hits) {
        currentStrategy = getRandomStrategy();
    }

    @Override
    public int getAttackDelay(AttackStance stance) {
        return currentStrategy.getAttackDelay(stance);
    }

    @Override
    public int getAttackDistance(AttackStance stance) {
        return currentStrategy.getAttackDistance(stance);
    }

    @Override
    public CombatHit[] getHits(Mob attacker, Actor defender) {
        return currentStrategy.getHits(attacker, defender);
    }

    @Override
    public Optional<AttackModifier> getModifier(Mob attacker, Actor defender) {
        return currentStrategy.getModifier(attacker, defender);
    }

    protected final CombatStrategy<? super Mob> getRandomStrategy() {
        return strategies.get((int) (Math.random() * strategies.size()));
    }
}
