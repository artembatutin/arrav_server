package net.arrav.content.skill.agility.obstacle;

import net.arrav.task.Task;
import net.arrav.world.Animation;
import net.arrav.world.entity.actor.move.ForcedMovement;
import net.arrav.world.entity.actor.move.ForcedMovementManager;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;

/**
 * Holds functionality for obstacle activity's when trying to cross them.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class ObstacleActivity {
	
	/**
	 * The start position of this action.
	 */
	private final Position start;
	
	/**
	 * The end position of this action.
	 */
	private final Position destination;
	
	/**
	 * The animation for this action.
	 */
	private final Animation animation;
	
	/**
	 * The requirement required to pass this obstacle.
	 */
	private final int requirement;
	
	/**
	 * The experience gained for this action.
	 */
	private final double experience;
	
	/**
	 * Constructs a new {@link ObstacleActivity}.
	 * @param start {@link #start}.
	 * @param destination {@link #destination}.
	 * @param animation {@link #animation}.
	 * @param requirement {@link #requirement}.
	 * @param experience {@link #experience}.
	 */
	public ObstacleActivity(Position start, Position destination, Animation animation, int requirement, double experience) {
		this.start = start;
		this.destination = destination;
		this.animation = animation;
		this.requirement = requirement;
		this.experience = experience;
	}
	
	/**
	 * Constructs a new {@link ObstacleActivity}.
	 * @param start {@link #start}.
	 * @param destination {@link #destination}.
	 * @param animation {@link #animation}.
	 * @param requirement {@link #requirement}.
	 * @param experience {@link #experience}.
	 */
	public ObstacleActivity(Position start, Position destination, int animation, int requirement, double experience) {
		this(start, destination, new Animation(animation), requirement, experience);
	}
	
	/**
	 * Determines if this obstacle can be travelled back.
	 * @return on default false.
	 */
	public boolean travelBack() {
		return false;
	}
	
	/**
	 * The delay until the action is executed.
	 * @return the identifier for this delay.
	 */
	public int getDelay() {
		return 0;
	}
	
	/**
	 * Determines if this activity should be executed instantly rather then after the delay
	 * when submitting the task.
	 * @return <true> if it should be instant, <false> otherwise.
	 */
	public boolean instant() {
		return false;
	}
	
	/**
	 * The flag which identifies if this action could be initialized or not.
	 * @return <true> if the player could execute, <false> otherwise.
	 */
	public final boolean init(Player player) {
		return canExecute(player);
	}
	
	/**
	 * Holds functionality for cancelling this task if the player can't execute the course.
	 */
	public boolean canExecute(Player player) {
		return true;
	}
	
	/**
	 * Holds functionality for submitting to the task.
	 */
	public void onSubmit(Player player) {

	}
	
	/**
	 * The functionality handled on execution.
	 * @param player the player this task is attached to.
	 * @param t the task for this obstacle action.
	 */
	public void execute(Player player, Task t) {

	}
	
	/**
	 * Holds functionality for cancelling the task.
	 */
	public void onCancel(Player player) {

	}
	
	/**
	 * Executes an {@Link ForcedMovement} action before instantiation of any further action.
	 */
	public static void executeForcedMovementAction(Player player, Position beggining_position, Position ending_position, int first_speed, int second_speed, Animation animation_id) {
		player.setForcedMovement(new ForcedMovement(player));
		ForcedMovement movement = player.getForcedMovement();
		movement.setActive(true);
		movement.setFirst(beggining_position);
		movement.setSecond(ending_position);
		movement.setFirstSpeed(first_speed);
		movement.setSecondSpeed(second_speed);
		movement.setAnimation(animation_id);
		ForcedMovementManager.submit(player, movement);
	}
	
	/**
	 * @return {@link #start}.
	 */
	public final Position getStart() {
		return start;
	}
	
	/**
	 * @return {@link #destination}.
	 */
	public final Position getDestination() {
		return destination;
	}
	
	/**
	 * @return {@link #animation}.
	 */
	public final Animation getAnimation() {
		return animation;
	}
	
	/**
	 * @return {@link #requirement}
	 */
	public final int getRequirement() {
		return requirement;
	}
	
	/**
	 * @return {@link #experience}.
	 */
	public final double getExperience() {
		return experience;
	}
}
