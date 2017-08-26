package net.edge.content.combat.strategy.npc.boss.kbd;

import net.edge.content.combat.CombatProjectileDefinition;
import net.edge.content.combat.CombatUtil;
import net.edge.content.combat.attack.FightType;
import net.edge.content.combat.hit.CombatHit;
import net.edge.content.combat.strategy.npc.impl.DragonfireStrategy;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;

public class KBDShockStrategy extends DragonfireStrategy {

    KBDShockStrategy() {
        super(CombatProjectileDefinition.getDefinition("KBD shock"));
    }

    @Override
    public int getAttackDistance(Mob attacker, FightType fightType) {
        return 10;
    }

    @Override
    public CombatHit[] getHits(Mob attacker, Actor defender) {
        return new CombatHit[] { CombatUtil.generateDragonfire(attacker, defender, 650, true) };
    }

}
