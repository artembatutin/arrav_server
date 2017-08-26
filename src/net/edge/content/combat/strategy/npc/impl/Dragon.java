package net.edge.content.combat.strategy.npc.impl;

import net.edge.content.combat.CombatProjectileDefinition;
import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.content.combat.strategy.npc.MultiStrategy;
import net.edge.content.combat.strategy.npc.NpcMeleeStrategy;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.container.impl.Equipment;

/** @author Michael | Chex */
public class Dragon extends MultiStrategy {
    private static final DragonfireStrategy DRAGONFIRE = new DragonfireStrategy(CombatProjectileDefinition.getDefinition("Dragonfire"));
    private static final CombatStrategy<Mob>[] STRATEGIES = createStrategyArray(NpcMeleeStrategy.INSTANCE, DRAGONFIRE);

    public Dragon() {
        currentStrategy = NpcMeleeStrategy.INSTANCE;
    }

    @Override
    public void attack(Mob attacker, Actor defender, Hit hit) {
        super.attack(attacker, defender, hit);
        if (defender.isPlayer()) {
            Player player = defender.toPlayer();

            if (player.getEquipment().getId(Equipment.SHIELD_SLOT) == 11283) {
                // TODO: dragonfire shield effects to defender here
            }
        }
    }

    @Override
    public void finish(Mob attacker, Actor defender) {
        currentStrategy.finish(attacker, defender);
        currentStrategy = randomStrategy(STRATEGIES);
    }

    @Override
    public int getAttackDelay(Mob attacker, Actor defender, FightType fightType) {
        return attacker.getDefinition().getAttackDelay();
    }

}
