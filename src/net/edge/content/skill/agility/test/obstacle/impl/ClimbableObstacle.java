package net.edge.content.skill.agility.test.obstacle.impl;

import net.edge.content.skill.agility.test.obstacle.Obstacle;
import net.edge.task.Task;
import net.edge.world.Animation;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 7-8-2017.
 */
public final class ClimbableObstacle extends Obstacle {

    public ClimbableObstacle(int[] ids, Position[] start, Position end, Animation animation, int requirement, double experience, boolean travelback) {
        super(ids, start, end, animation, requirement, experience, 3, travelback);
    }

    public ClimbableObstacle(int id, Position[] start, Position end, Animation animation, int requirement, double experience, boolean travelback) {
        super(new int[]{id}, start, end, animation, requirement, experience, 3, travelback);
    }

    public ClimbableObstacle(int id, Position start, Position end, Animation animation, int requirement, double experience, boolean travelback) {
        super(new int[]{id}, new Position[]{start}, end, animation, requirement, experience, 3, travelback);
    }

    public ClimbableObstacle(int id, Position start, Position end, int animationId, int requirement, double experience, boolean travelback) {
        super(new int[]{id}, new Position[]{start}, end, new Animation(animationId), requirement, experience, 3, travelback);
    }

    public ClimbableObstacle(int id, Position[] start, Position end, int animationId, int requirement, double experience, boolean travelback) {
        this(new int[]{id}, start, end, new Animation(animationId), requirement, experience, travelback);
    }

    @Override
    public void initialize(Player player) {
        player.animation(animation);
    }

    @Override
    public void execute(Player player, Task t) {
        player.move(end);
    }
}
