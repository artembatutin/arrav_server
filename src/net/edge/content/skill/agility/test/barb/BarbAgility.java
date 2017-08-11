package net.edge.content.skill.agility.test.barb;

import net.edge.action.impl.ObjectAction;
import net.edge.content.skill.agility.test.Agility;
import net.edge.content.skill.agility.test.gnome.JumpOverBarrier;
import net.edge.content.skill.agility.test.gnome.PoleSwing;
import net.edge.content.skill.agility.test.obstacle.Obstacle;
import net.edge.content.skill.agility.test.obstacle.impl.ClimbableObstacle;
import net.edge.content.skill.agility.test.obstacle.impl.FMObstacle;
import net.edge.content.skill.agility.test.obstacle.impl.WalkableObstacle;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;
import net.edge.world.object.GameObject;

import java.util.OptionalInt;
import java.util.function.Function;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 4-8-2017.
 */
public final class BarbAgility extends Agility {

    /**
     * Constructs a new {@link Agility}.
     * @param player   {@link #player}.
     * @param object   {@link #object}.
     * @param crossing {@link #crossing}.
     */
   private BarbAgility(Player player, GameObject object, Obstacle crossing) {
        super(player, object, crossing);
    }

    public static void action() {
        for(Obstacles obstacleFunction : Obstacles.values()) {
            ObjectAction action = new ObjectAction() {

                @Override
                public boolean click(Player player, GameObject object, int click) {
                    Obstacle obstacle = obstacleFunction.obstacles.apply(player);

                    if(!obstacle.findProperPosition(player)) {
                        new BarbAgility(player, object, obstacle).start();
                        return true;
                    }

                    Position walk = null;
                    Position[] targets = obstacle.start;
                    if(targets.length == 0) {
                        walk = targets[0];
                    } else {
                        double dist = 0;
                        for(Position p : targets) {
                            double d = player.getPosition().getDistance(p);
                            if(dist == 0 || dist > d) {
                                walk = p;
                                dist = d;
                            }
                        }
                    }
                    final Position dest = walk;
                    player.getMovementQueue().smartWalk(walk);
                    player.getMovementListener().append(() -> {
                        if(player.getPosition().same(dest)) {
                            new BarbAgility(player, object, obstacleFunction.obstacles.apply(player)).start();
                        }
                    });

                    return true;
                }
            };
            for(int objId : obstacleFunction.ids) {
                action.registerFirst(objId);
                action.registerFirst(objId + 42003);
            }
        }
    }

    @Override
    public double experience() {
        return 39;
    }

    public enum Obstacles {
        ROPE_SWING(43526, p -> new FMObstacle(90, OptionalInt.of(3), new Position[]{new Position(2551, 3554), new Position(2552, 3554)}, new Position(p.getPosition().getX(), 3549), 751, 35, 22) {
            @Override
            public boolean findProperPosition(Player player) {
                return false;
            }
            @Override
            public boolean crossable(Player player) {
                if(player.getPosition().same(new Position(2552, 3554, 0)) || player.getPosition().same(new Position(2551, 3554, 0))) {
                    return true;
                }
                player.message("You must be standing infront one of the ropes.");
                return false;
            }
        });

        public final int[] ids;

        Function<Player, Obstacle> obstacles;

        Obstacles(int[] ids, Function<Player, Obstacle> obstacles) {
            this.obstacles = obstacles;
            this.ids = ids;
        }

        Obstacles(int id, Function<Player, Obstacle> obstacles) {
            this.obstacles = obstacles;
            this.ids = new int[]{id};
        }
    }
}
