package net.edge.content.combat.strategy.npc.impl;

import net.edge.content.combat.CombatProjectileDefinition;
import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.content.combat.strategy.npc.MultiStrategy;
import net.edge.content.combat.strategy.npc.NpcMeleeStrategy;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;

/** @author Michael | Chex */
public class DragonStrategy extends MultiStrategy {
    private static final DragonfireStrategy CHROMATIC_DRAGONFIRE = new DragonfireStrategy(CombatProjectileDefinition.getDefinition("Chromatic ragonfire"));
    private static final DragonfireStrategy METALIC_DRAGONFIRE = new DragonfireStrategy(CombatProjectileDefinition.getDefinition("Metalic ragonfire"));

    private static final CombatStrategy<Mob>[] METALIC_STRATEGIES = createStrategyArray(NpcMeleeStrategy.INSTANCE, CHROMATIC_DRAGONFIRE);
    private static final CombatStrategy<Mob>[] CHROMATIC_STRATEGIES = createStrategyArray(NpcMeleeStrategy.INSTANCE, METALIC_DRAGONFIRE);

    private final CombatStrategy<Mob>[] strategies;

    public DragonStrategy(boolean chromatic) {
        currentStrategy = NpcMeleeStrategy.INSTANCE;
        strategies = chromatic ? CHROMATIC_STRATEGIES : METALIC_STRATEGIES;
    }

    @Override
    public void finish(Mob attacker, Actor defender) {
        currentStrategy.finish(attacker, defender);
        currentStrategy = randomStrategy(strategies);
    }

    @Override
    public int getAttackDelay(Mob attacker, Actor defender, FightType fightType) {
        return attacker.getDefinition().getAttackDelay();
    }

}
