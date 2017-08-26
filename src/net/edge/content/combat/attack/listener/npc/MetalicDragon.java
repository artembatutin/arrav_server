package net.edge.content.combat.attack.listener.npc;

import net.edge.content.combat.attack.listener.NpcCombatListenerSignature;
import net.edge.content.combat.attack.listener.SimplifiedListener;
import net.edge.content.combat.hit.Hit;
import net.edge.content.combat.strategy.CombatStrategy;
import net.edge.content.combat.strategy.npc.NpcMeleeStrategy;
import net.edge.content.combat.strategy.npc.impl.DragonfireStrategy;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.Mob;

import static net.edge.content.combat.CombatProjectileDefinition.getDefinition;
import static net.edge.content.combat.CombatUtil.createStrategyArray;
import static net.edge.content.combat.CombatUtil.randomStrategy;

/** @author Michael | Chex */
@NpcCombatListenerSignature(npcs = {
    1590, 1591, 1592, 3590, 5363, 8424,
    10776, 10777, 10778, 10779, 10780, 10781
})
public class MetalicDragon extends SimplifiedListener<Mob> {
    private static CombatStrategy<Mob>[] STRATEGIES;

    static {
        try {
            DragonfireStrategy DRAGONFIRE = new DragonfireStrategy(getDefinition("Metalic dragonfire"));
            STRATEGIES = createStrategyArray(NpcMeleeStrategy.INSTANCE, DRAGONFIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Mob attacker, Actor defender, Hit[] hits) {
        attacker.getCombat().setStrategy(randomStrategy(STRATEGIES));
    }

}


