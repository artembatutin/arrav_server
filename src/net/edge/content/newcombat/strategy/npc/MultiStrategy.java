package net.edge.content.newcombat.strategy.npc;

import net.edge.content.combat.weapon.FightType;
import net.edge.content.newcombat.CombatType;
import net.edge.content.newcombat.attack.AttackModifier;
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
    public int getAttackDelay(FightType fightType) {
        return currentStrategy.getAttackDelay(fightType);
    }

    @Override
    public int getAttackDistance(FightType fightType) {
        return currentStrategy.getAttackDistance(fightType);
    }

    @Override
    public CombatHit[] getHits(Mob attacker, Actor defender) {
        return currentStrategy.getHits(attacker, defender);
    }

    @Override
    public Optional<AttackModifier> getModifier(Mob attacker, Actor defender) {
        return currentStrategy.getModifier(attacker, defender);
    }

    @Override
    public CombatType getCombatType() {
        return currentStrategy.getCombatType();
    }

    protected final CombatStrategy<? super Mob> getRandomStrategy() {
        return strategies.get((int) (Math.random() * strategies.size()));
    }
}
