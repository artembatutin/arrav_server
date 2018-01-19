package net.arrav.content.skill.agility.obstacle.impl;

import net.arrav.content.skill.agility.obstacle.ObstacleActivity;
import net.arrav.world.Animation;
import net.arrav.world.entity.actor.move.ForcedMovement;
import net.arrav.world.entity.actor.move.ForcedMovementManager;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;

import java.util.OptionalInt;

/**
 * The forced movement obstacle action which will walk a player starting from the start position
 * to the destination with it's respective {@link ForcedMovement} mask.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public class Movable extends ObstacleActivity {
	
	/**
	 * The speed for this forced movement mask.
	 */
	private final int speed;
	
	/**
	 * The timer for this forced movement mask.
	 */
	private OptionalInt timer = OptionalInt.empty();
	
	/**
	 * Constructs a new {@link Movable} Obstacle Activity.
	 * @param start       {@link #getStart()}.
	 * @param destination {@link #getDestination().
	 * @param animation   {@link #getAnimation()}.
	 * @param speed       {@link #speed}.
	 * @param timer       {@link #timer}.
	 * @param requirement {@link #getRequirement()}.
	 * @param experience  {@link #getExperience()}.
	 */
	public Movable(Position start, Position destination, Animation animation, int speed, OptionalInt timer, int requirement, double experience) {
		super(start, destination, animation, requirement, experience);
		
		this.speed = speed;
		this.timer = timer;
	}
	
	/**
	 * Constructs a new {@link Movable} Obstacle Activity.
	 * @param start       {@link #getStart()}.
	 * @param destination {@link #getDestination().
	 * @param animation   {@link #getAnimation()}.
	 * @param speed       {@link #speed}.
	 * @param timer       {@link #timer}.
	 * @param requirement {@link #getRequirement()}.
	 * @param experience  {@link #getExperience()}.
	 */
	public Movable(Position start, Position destination, Animation animation, int speed, int timer, int requirement, double experience) {
		this(start, destination, animation, speed, OptionalInt.of(timer), requirement, experience);
	}
	
	public Movable(Position start, Position destination, Animation animation, int speed, int requirement, double experience) {
		this(start, destination, animation, speed, OptionalInt.empty(), requirement, experience);
	}
	
	@Override
	public boolean canExecute(Player player) {
		if(!(travelBack() && player.getPosition().same(getDestination())) && !player.getPosition().same(getStart())) {
			player.message("You can't cross this obstacle from this position.");
			return false;
		}
		return true;
	}
	
	public void start(Player player) {
		ForcedMovement movement = new ForcedMovement(player);
		if(player.getPosition().same(getStart())) {
			movement.setFirst(getStart());
			movement.setSecond(getDestination());
		} else if(travelBack() && player.getPosition().same(getDestination())) {
			movement.setFirst(getDestination());
			movement.setSecond(getStart());
		}
		movement.setFirstSpeed(10);
		movement.setSecondSpeed(getSpeed());
		movement.setAnimation(getAnimation());
		timer.ifPresent(movement::setTimer);
		ForcedMovementManager.submit(player, movement);
	}
	
	/**
	 * Any functionality that should be executed before the actual task is submitted.
	 * @param player the player we need to execute this functionality for.
	 */
	public void prerequisites(Player player) {
	
	}
	
	@Override
	public void onSubmit(Player player) {
		prerequisites(player);
		start(player);
	}
	
	/**
	 * @return the speed
	 */
	public int getSpeed() {
		return speed;
	}
}
