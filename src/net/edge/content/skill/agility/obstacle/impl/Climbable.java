package net.edge.content.skill.agility.obstacle.impl;

import net.edge.task.Task;
import net.edge.content.skill.agility.obstacle.ObstacleActivity;
import net.edge.locale.Position;
import net.edge.world.Animation;
import net.edge.world.node.actor.player.Player;

/**
 * The climbable obstacle action which will move a player from a starting position to a new height
 * with a sudden delay while performing an animation.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public class Climbable extends ObstacleActivity {
	
	/**
	 * The delay after the player get's moved to the new position.
	 */
	private final int delay;
	
	/**
	 * Constructs a new {@link Climbable} Obstacle Activity.
	 * @param start       {@link #getStart()}.
	 * @param animation   {@link #getAnimation()}.
	 * @param delay       {@link #delay}.
	 * @param requirement {@link #getRequirement()}.
	 * @param experience  {@link #getExperience()}.
	 */
	public Climbable(Position start, Position end, Animation animation, int delay, int requirement, double experience) {
		super(start, end, animation, requirement, experience);
		this.delay = delay;
	}
	
	@Override
	public int getDelay() {
		return delay;
	}
	
	@Override
	public void onSubmit(Player player) {
		player.animation(getAnimation());
	}

	@Override
	public boolean canExecute(Player player) {
		return !player.getPosition().same(getDestination());
	}

	@Override
	public void execute(Player player, Task t) {
		player.move(getDestination());
	}
}
