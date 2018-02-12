package net.arrav.world.entity.actor.combat.strategy.npc.boss;

import com.google.common.collect.Multisets;
import net.arrav.net.packet.out.SendArrowEntity;
import net.arrav.net.packet.out.SendGraphic;
import net.arrav.task.Task;
import net.arrav.util.rand.RandomUtils;
import net.arrav.world.Animation;
import net.arrav.world.World;
import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.combat.CombatType;
import net.arrav.world.entity.actor.combat.hit.CombatHit;
import net.arrav.world.entity.actor.combat.hit.Hit;
import net.arrav.world.entity.actor.combat.strategy.npc.MultiStrategy;
import net.arrav.world.entity.actor.combat.strategy.npc.NpcMeleeStrategy;
import net.arrav.world.entity.actor.mob.Mob;
import net.arrav.world.locale.Position;

/**
 * Created by Dave/Ophion
 * Date: 12/02/2018
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
public class GiantMoleStrategy extends NpcMeleeStrategy {

    private static final int[][] COORDINATES = {{1760, 5164},
            {1781, 5151}, {1753, 5150}, {1738, 5155}, {1746, 5171},
            {1741, 5187}, {1738, 5209}, {1779, 5182}, {1754, 5206},
            {1738, 5225}, {1770, 5228}, {1778, 5236}, {1779, 5208},
            {1771, 5200}, {1779, 5188}, {1774, 5174}, {1763, 5184},
            {1757, 5185}, {1760, 5192}, {1751, 5174}};

    @Override
    public boolean canAttack(Mob attacker, Actor defender) {
        return defender.isPlayer();
    }

    /**
     * @param attacker the attacker whom hit the character.
     * @TODO: Get the correct gfx ids, and fix entity arrow(currently freezes screen)
     */
    @Override
    public void block(Actor attacker, Mob defender, Hit hit, CombatType combatType) {
        if (defender.getCurrentHealth() < (defender.getMaxHealth() / 2) && RandomUtils.inclusive(100) < 25) {
            int coords = RandomUtils.random(COORDINATES.length - 1);
            defender.animation(new Animation(3314, Animation.AnimationPriority.HIGH));
            for (int i = 0; i < 3; i++) {
                attacker.toPlayer().out(new SendGraphic(1271, new Position((defender.getPosition().getX() + 1 - i), (defender.getPosition().getY() + 1 - i)), 0));

            }
            //attacker.toPlayer().out(new SendArrowEntity(defender));
            World.get().submit(new Task(3, false) {
                @Override
                public void execute() {
                    this.cancel();
                    defender.move(new Position(COORDINATES[coords][0], COORDINATES[coords][1], 0));
                    defender.animation(new Animation(3315, Animation.AnimationPriority.HIGH));
                }
            });
            //attacker.toPlayer().out(new SendArrowPosition(npc.getPosition(), 6));
        }
       // defender.getCombat().attack(attacker);
    }
}