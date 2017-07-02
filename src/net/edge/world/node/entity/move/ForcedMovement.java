package net.edge.world.node.entity.move;

import net.edge.task.Task;
import net.edge.locale.Position;
import net.edge.world.node.entity.EntityNode;
import net.edge.world.Animation;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * Represents a single forced movement action.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class ForcedMovement {
	
	/**
	 * The character using this {@link ForcedMovement}.
	 */
	private final EntityNode character;
	
	/**
	 * The first movement position for this {@link ForcedMovement}.
	 */
	private Position first;
	
	/**
	 * The second movement position for this {@link ForcedMovement}.
	 */
	private Position second;
	
	/**
	 * The first movement animation for this {@link ForcedMovement}.
	 */
	private Animation animation;
	
	/**
	 * The direction we should face.
	 */
	private ForcedMovementDirection direction;
	
	/**
	 * The first movement speed.
	 */
	private int firstSpeed;
	
	/**
	 * The second movement speed.
	 */
	private int secondSpeed;
	
	/**
	 * Determines if the timer should be used instead of automatically generating.
	 */
	private OptionalInt timer = OptionalInt.empty();
	
	/**
	 * The flag which checks if a forced movement is active.
	 */
	private boolean active = false;
	
	/**
	 * On destination process.
	 */
	protected Optional<Runnable> onDestination = Optional.empty();

	/**
	 * The walking speed.
	 */
	public static final int RUNNING_SPEED = 60;
	
	/**
	 * The running speed.
	 */
	public static final int WALKING_SPEED = 100;
	
	/**
	 * The walking animation.
	 */
	private static final Animation WALK_ANIMATION = new Animation(819);
	
	/**
	 * Constructs a new {@link ForcedMovement} {@link Task}.
	 * @param character {@link #character}.
	 */
	public ForcedMovement(EntityNode character) {
		this.character = character;
		this.first = character.getPosition();
		this.animation = WALK_ANIMATION;
		this.firstSpeed = 10;
		this.secondSpeed = 60;
	}
	
	/**
	 * Constructs a new {@code ForceMovement} {@code Task}.
	 * @param character the character.
	 * @param second    the destination.
	 * @param animation the animation.
	 */
	public static ForcedMovement create(EntityNode character, Position second, Animation animation) {
		ForcedMovement movement = new ForcedMovement(character);
		movement.setSecond(second);
		if(animation != null) {
			movement.setAnimation(animation);
		}
		return movement;
	}
	
	/**
	 * Constructs a new {@code ForceMovement} {@code Task}.
	 * @param character the character.
	 * @param second    the destination.
	 * @param animation the animation.
	 */
	public static ForcedMovement create(EntityNode character, Position second, boolean animation) {
		return create(character, second, animation ? WALK_ANIMATION : null);
	}
	
	/**
	 * Constructs a new {@code ForceMovement} {@code Task}.
	 * @param character the character.
	 * @param second    the destination.
	 */
	public static ForcedMovement create(EntityNode character, Position second) {
		return create(character, second, false);
	}
	
	/**
	 * Gets the direction value.
	 * @param start       The start location.
	 * @param destination The second.
	 * @return The direction object.
	 */
	public static ForcedMovementDirection direction(Position start, Position destination) {
		Position delta = Position.delta(start, destination);
		return ForcedMovementDirection.getDirection(delta.getX(), delta.getY());
	}

	/**
	 * Submits this forced movement.
	 */
	public void submit() {
		ForcedMovementManager.submit(character, this);
	}
	
	/**
	 * @return the first
	 */
	public Position getFirst() {
		return first;
	}
	
	/**
	 * Sets the first position.
	 * @param first the first position to set.
	 * @return the forced movement.
	 */
	public ForcedMovement setFirst(Position first) {
		this.first = first.copy();
		return this;
	}
	
	/**
	 * @return the second
	 */
	public Position getSecond() {
		return second;
	}
	
	/**
	 * Sets the second position.
	 * @param second the second position to set.
	 * @return the forced movement.
	 */
	public ForcedMovement setSecond(Position second) {
		direction = direction(first, second);
		this.second = second.copy();
		return this;
	}
	
	/**
	 * @return the animation.
	 */
	public Animation getAnimation() {
		return animation;
	}
	
	/**
	 * Sets the animation.
	 * @param animation the animation to set.
	 * @return the forced movement.
	 */
	public ForcedMovement setAnimation(Animation animation) {
		this.animation = animation;
		return this;
	}
	
	/**
	 * Sets the animation.
	 * @param animation the animation to set.
	 * @return the forced movement.
	 */
	public ForcedMovement setAnimation(int animation) {
		this.animation = new Animation(animation);
		return this;
	}
	
	/**
	 * @return the direction
	 */
	public ForcedMovementDirection getDirection() {
		return direction;
	}
	
	public ForcedMovement setDirection(ForcedMovementDirection direction) {
		this.direction = direction;
		return this;
	}
	
	/**
	 * @return the firstSpeed
	 */
	public int getFirstSpeed() {
		return firstSpeed;
	}
	
	/**
	 * Sets the first speed.
	 * @param firstSpeed the firstSpeed to set.
	 * @return the forced movement.
	 */
	public ForcedMovement setFirstSpeed(int firstSpeed) {
		this.firstSpeed = firstSpeed;
		return this;
	}
	
	/**
	 * @return the secondSpeed
	 */
	public int getSecondSpeed() {
		return secondSpeed;
	}
	
	/**
	 * Sets the second speed.
	 * @param secondSpeed the secondSpeed to set.
	 * @return the forced movement.
	 */
	public ForcedMovement setSecondSpeed(int secondSpeed) {
		this.secondSpeed = secondSpeed;
		return this;
	}
	
	/**
	 * @return the timer
	 */
	public OptionalInt getTimer() {
		return timer;
	}
	
	/**
	 * @param timer the time to set.
	 */
	public void setTimer(int timer) {
		this.timer = OptionalInt.of(timer);
	}
	
	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	/**
	 * @return the character
	 */
	public EntityNode getCharacter() {
		return character;
	}

	public ForcedMovement onDestination(Runnable execute) {
		this.onDestination = Optional.of(execute);
		return this;
	}
}
