package net.edge.content.skill.agility.test.obstacle;

import net.edge.task.Task;
import net.edge.world.Animation;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.locale.Position;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 4-8-2017.
 */
public abstract class Obstacle {

    public final int[] ids;

    public final Position[] start;

    public final Position end;

    public final Animation animation;

    public final int requirement;

    public final double experience;

    public final int delay;

    public final boolean travelback;

    public Obstacle(int[] ids, Position[] start, Position end, Animation animation, int requirement, double experience, int delay, boolean travelback) {
        this.ids = ids;
        this.start = start;
        this.end = end;
        this.animation = animation;
        this.requirement = requirement;
        this.experience = experience;
        this.delay = delay;
        this.travelback = travelback;
    }

    public Obstacle(int[] ids, Position start, Position end, int animation, int requirement, double experience, int delay, boolean travelback) {
        this(ids, new Position[]{start}, end, new Animation(animation), requirement, experience, delay, travelback);
    }

    public Obstacle(int ids, Position start, Position end, int animation, int requirement, double experience, int delay, boolean travelback) {
        this(new int[]{ids}, new Position[]{start}, end, new Animation(animation), requirement, experience, delay, travelback);
    }

    public Obstacle(int[] ids, Position[] start, Position end, int animation, int requirement, double experience, int delay, boolean travelback) {
        this(ids, start, end, new Animation(animation), requirement, experience, delay, travelback);
    }

    public boolean crossable(Player player) {
        return true;
    }

    public void initialize(Player player) {

    }

    public void execute(Player player, Task t) {

    }

    public void onStop(Player player) {

    }

}
