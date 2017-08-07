package net.edge.content.skill.agility.test.gnome;

import net.edge.action.impl.ObjectAction;
import net.edge.content.skill.agility.impl.gnome.GnomeStrongholdAgility;
import net.edge.content.skill.agility.impl.gnome.impl.JumpOverBarrier;
import net.edge.content.skill.agility.impl.gnome.impl.PoleSwing;
import net.edge.content.skill.agility.obstacle.ObstacleAction;
import net.edge.content.skill.agility.obstacle.ObstacleType;
import net.edge.content.skill.agility.obstacle.impl.Climbable;
import net.edge.content.skill.agility.obstacle.impl.Movable;
import net.edge.content.skill.agility.obstacle.impl.Walkable;
import net.edge.content.skill.agility.test.Agility;
import net.edge.content.skill.agility.test.obstacle.Obstacle;
import net.edge.content.skill.agility.test.obstacle.impl.ClimbableObstacle;
import net.edge.content.skill.agility.test.obstacle.impl.WalkableObstacle;
import net.edge.world.Animation;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;
import net.edge.world.object.GameObject;

import java.util.Optional;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 4-8-2017.
 */
public final class GnomeAgility extends Agility {

    /**
     * Constructs a new {@link Agility}.
     * @param player   {@link #player}.
     * @param object   {@link #object}.
     * @param crossing {@link #crossing}.
     */
    public GnomeAgility(Player player, GameObject object, Obstacle crossing) {
        super(player, object, crossing);
    }

    /**
     * The obstacles this agility has in total.
     * @return
     */
    public static Obstacle[] obstacles() {
        return new Obstacle[]{LOG_BALANCE};
    }

    public static void action() {
        for(Obstacle obstacles : obstacles()) {
            ObjectAction action = new ObjectAction() {

                @Override
                public boolean click(Player player, GameObject object, int click) {
                    new GnomeAgility(player, object, obstacles).start();
                    return false;
                }
            };
            for(int objId : obstacles.ids) {
                action.registerFirst(objId);
            }
        }
    }

    @Override
    public double experience() {
        return 39;
    }

    private static final WalkableObstacle LOG_BALANCE = new WalkableObstacle(2295, new Position(2474, 3436), new Position(2474, 3429), 762, 1, 7, false);

    private static final Position[] OBSTACLE_NET_POSITIONS = new Position[]{new Position(2476, 3426), new Position(2475, 3426), new Position(2474, 3426), new Position(2473, 3426), new Position(2472, 3426), new Position(2471, 3426)};

    private static final ClimbableObstacle getObstacleNet(Player player) {
        return new ClimbableObstacle(2285, OBSTACLE_NET_POSITIONS, new Position(player.getPosition().getX(), 3424), 828, 1, 7.5, false);
    }
}
