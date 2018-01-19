package net.arrav.content.skill.agility.test.obstacle;

import net.arrav.task.Task;
import net.arrav.world.Animation;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 4-8-2017.
 */
public abstract class Obstacle {
	
	public final Position[] start;
	
	public final Position end;
	
	public final Animation animation;
	
	public final int requirement;
	
	public final double experience;
	
	public final int delay;
	
	public final boolean instant;
	
	public Obstacle(Position[] start, Position end, Animation animation, int requirement, double experience, int delay, boolean instant) {
		this.start = start;
		this.end = end;
		this.animation = animation;
		this.requirement = requirement;
		this.experience = experience;
		this.delay = delay;
		this.instant = instant;
	}
	
	public Obstacle(Position[] start, Position end, Animation animation, int requirement, double experience, int delay) {
		this(start, end, animation, requirement, experience, delay, false);
	}
	
	public Obstacle(Position start, Position end, int animation, int requirement, double experience, int delay) {
		this(new Position[]{start}, end, new Animation(animation), requirement, experience, delay);
	}
	
	public Obstacle(Position[] start, Position end, int animation, int requirement, double experience, int delay) {
		this(start, end, new Animation(animation), requirement, experience, delay);
	}
	
	public Obstacle(Position start, Position end, int animation, int requirement, double experience, int delay, boolean instant) {
		this(new Position[]{start}, end, new Animation(animation), requirement, experience, delay, instant);
	}
	
	public boolean crossable(Player player) {
		return true;
	}
	
	public boolean findProperPosition(Player player) {
		return true;
	}
	
	public boolean travelback() {
		return false;
	}
	
	public void initialize(Player player) {
	
	}
	
	public void execute(Player player, Task t) {
	
	}
	
	public void onStop(Player player) {
	
	}
	
}
