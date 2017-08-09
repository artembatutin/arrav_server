package net.edge.world.entity.actor.mob.strategy.impl;

import net.edge.content.combat.CombatHit;
import net.edge.content.combat.CombatType;
import net.edge.net.packet.out.SendArrowEntity;
import net.edge.net.packet.out.SendArrowPosition;
import net.edge.net.packet.out.SendGraphic;
import net.edge.task.LinkedTaskSequence;
import net.edge.task.Task;
import net.edge.util.rand.RandomUtils;
import net.edge.world.Animation;
import net.edge.world.Graphic;
import net.edge.world.Projectile;
import net.edge.world.World;
import net.edge.world.entity.EntityState;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.mob.impl.GiantMole;
import net.edge.world.entity.actor.mob.strategy.DynamicStrategy;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;

/**
 * Created by Dave/Ophion
 * Date: 09/08/2017
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
public class GiantMoleStrategy extends DynamicStrategy <GiantMole> {

    public GiantMoleStrategy(GiantMole npc) {
        super(npc);
    }

    private static final int[][] COORDINATES = { { 1760, 5164 },
            { 1781, 5151 }, { 1753, 5150 }, { 1738, 5155 }, { 1746, 5171 },
            { 1741, 5187 }, { 1738, 5209 }, { 1779, 5182 }, { 1754, 5206 },
            { 1738, 5225 }, { 1770, 5228 }, { 1778, 5236 }, { 1779, 5208 },
            { 1771, 5200 }, { 1779, 5188 }, { 1774, 5174 }, { 1763, 5184 },
            { 1757, 5185 }, { 1760, 5192 }, { 1751, 5174 } };

    @Override
    public boolean canOutgoingAttack(Actor victim) {
        return true;
    }

    @Override
    public CombatHit outgoingAttack(Actor victim) {
        npc.animation(new Animation(npc.getDefinition().getAttackAnimation()));
        return new CombatHit(npc, victim, 1, CombatType.MELEE, false);
    }

    /**
     * @TODO: Get the correct gfx ids, and fix entity arrow(currently freezes screen)
     * @param attacker the attacker whom hit the character.
     * @param data     the combat session data chained to this hit.
     */
    @Override
    public void incomingAttack(Actor attacker, CombatHit data) {
        if(npc.getCurrentHealth() < (npc.getMaxHealth() * 0.5) && RandomUtils.inclusive(100) < 25) {
            int coords = RandomUtils.random(COORDINATES.length - 1);
            npc.animation(new Animation(3314, Animation.AnimationPriority.HIGH));
            for(int i=0; i<3; i++) {
                attacker.toPlayer().out(new SendGraphic(1271, new Position((npc.getPosition().getX() + 1 - i), (npc.getPosition().getY() + 1 - i)), 0));
            }
            //attacker.toPlayer().out(new SendArrowEntity(npc));
            World.get().submit(new Task(3, false) {
                @Override
                public void execute() {
                    this.cancel();
                    npc.move(new Position(COORDINATES[coords][0], COORDINATES[coords][1], 0));
                    npc.animation(new Animation(3315, Animation.AnimationPriority.HIGH));
                }
            });
            //attacker.toPlayer().out(new SendArrowPosition(npc.getPosition(), 6));
        }
    }

    @Override
    public int attackDelay() {
        return 6;
    }

    @Override
    public int attackDistance() {
        return 2;
    }

    @Override
    public boolean hitBack() {
        return true;
    }
}
