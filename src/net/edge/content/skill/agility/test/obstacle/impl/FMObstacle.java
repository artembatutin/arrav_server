package net.edge.content.skill.agility.test.obstacle.impl;

import net.edge.content.skill.agility.test.obstacle.Obstacle;
import net.edge.world.Animation;
import net.edge.world.entity.actor.move.ForcedMovement;
import net.edge.world.entity.actor.move.ForcedMovementManager;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;

import java.util.OptionalInt;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 10-8-2017.
 */
public class FMObstacle extends Obstacle {

    /**
     * The speed for this forced movement mask.
     */
    private final int speed;

    /**
     * The timer for this forced movement mask.
     */
    private final OptionalInt timer;

    public FMObstacle(int speed, OptionalInt timer, Position[] start, Position end, Animation animation, int requirement, double experience) {
        super(start, end, animation, requirement, experience, 0);
        this.speed = speed;
        this.timer = timer;
    }

    public FMObstacle(int speed, OptionalInt timer, Position start, Position end, Animation animation, int requirement, double experience) {
        super(new Position[]{start}, end, animation, requirement, experience, 0);
        this.speed = speed;
        this.timer = timer;
    }

    public FMObstacle(int speed, OptionalInt timer, Position start, Position end, int animationId, int requirement, double experience) {
        super(new Position[]{start}, end, new Animation(animationId), requirement, experience, 0);
        this.speed = speed;
        this.timer = timer;
    }

    public FMObstacle(int speed, OptionalInt timer, Position start[], Position end, int animationId, int requirement, double experience) {
        super(start, end, new Animation(animationId), requirement, experience, 0);
        this.speed = speed;
        this.timer = timer;
    }

    public void start(Player player) {
        ForcedMovement movement = new ForcedMovement(player);
        movement.setFirst(player.getPosition());
        movement.setSecond(travelback() && player.getPosition().same(end) ? start[0] : end);
        movement.setFirstSpeed(10);
        movement.setSecondSpeed(speed);
        movement.setAnimation(animation);
        timer.ifPresent(movement::setTimer);
        ForcedMovementManager.submit(player, movement);
    }

    @Override
    public void initialize(Player player) {
        start(player);
    }

}
