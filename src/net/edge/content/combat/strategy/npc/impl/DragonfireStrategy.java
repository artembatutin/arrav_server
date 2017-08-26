package net.edge.content.combat.strategy.npc.impl;

import net.edge.content.combat.CombatProjectileDefinition;
import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.strategy.npc.NpcMagicStrategy;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.container.impl.Equipment;

public class DragonfireStrategy extends NpcMagicStrategy {

    public DragonfireStrategy(CombatProjectileDefinition projectileDefinition) {
        super(projectileDefinition);
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
    public int getAttackDistance(Mob attacker, FightType fightType) {
        return 1;
    }

    @Override
    public CombatHit[] getHits(Mob attacker, Actor defender) {
        return new CombatHit[]{CombatUtil.generateDragonfire(attacker, defender, 600, true)};
    }

}