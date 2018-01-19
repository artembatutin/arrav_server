package net.arrav.world.entity.actor.move;

import net.arrav.task.Task;
import net.arrav.world.World;
import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.activity.ActivityManager;
import net.arrav.world.entity.actor.update.UpdateFlag;

/**
 * Holds functionality for the 0x400 mask.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class ForcedMovementManager {
	
	/**
	 * The movement to manage.
	 */
	private final ForcedMovement movement;
	
	/**
	 * The backing task running for this movement.
	 */
	private final Task t;
	
	/**
	 * Constructs a new {@link ForcedMovement} manager.
	 * @param movement the forced movement.
	 */
	private ForcedMovementManager(ForcedMovement movement) {
		this.movement = movement;
		this.t = new ForcedMovementTask(movement);
	}
	
	/**
	 * Checks if this forced movement can be submitted.
	 * @param character the player attempting to utilize the forced movement.
	 * @return <true> if the player can, <false> otherwise.
	 */
	private static boolean prerequisites(Actor character) {
		if(character.isMob()) {
			return true;
		}
		Player player = character.toPlayer();
		if(player.isTeleporting()) {
			player.message("You can't do this while teleporting.");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Submits the forced movement to the world.
	 * @param character the character doing the forced movement.
	 * @param movement  the movement to submit.
	 */
	public static void submit(Actor character, ForcedMovement movement) {
		if(prerequisites(character)) {
			ForcedMovementManager manager = new ForcedMovementManager(movement);
			movement.getCharacter().setForcedMovement(manager.movement);
			World.get().submit(manager.t);
		}
	}
	
	/**
	 * Submits the forced movement to the world.
	 * @param character         the character doing the forced movement.
	 * @param movement          the movement to submit.
	 * @param skipPrerequisites flag to skip  prerequisites.
	 */
	public static void submit(Actor character, ForcedMovement movement, boolean skipPrerequisites) {
		if(skipPrerequisites || prerequisites(character)) {
			ForcedMovementManager manager = new ForcedMovementManager(movement);
			movement.getCharacter().setForcedMovement(manager.movement);
			World.get().submit(manager.t);
		}
	}
	
	/**
	 * The backing task running for this task.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private static final class ForcedMovementTask extends Task {
		
		/**
		 * The forced movement this task is running for.
		 */
		private final ForcedMovement movement;
		
		/**
		 * Constructs a new {@link ForcedMovementTask}.
		 * @param movement {@link #movement}.
		 */
		ForcedMovementTask(ForcedMovement movement) {
			super(1, false);
			this.movement = movement;
		}
		
		/**
		 * The timer which moves the players to the destination when it hits zero.
		 */
		private int timer;
		
		/**
		 * Calculates the time to move the player to the destination.
		 * @return the time.
		 */
		private int calculateTimer() {
			int firstSpeed = movement.getFirstSpeed() * 30;
			int secondSpeed = (movement.getFirstSpeed() * 30) + (movement.getSecondSpeed() * 30 + 1);
			firstSpeed = (int) Math.ceil(movement.getFirst().getDistance(movement.getCharacter().getPosition()) / (firstSpeed * 0.1));
			secondSpeed = (int) Math.ceil(movement.getFirst().getDistance(movement.getSecond()) / (secondSpeed * 0.1));
			return movement.getTimer().isPresent() ? movement.getTimer().getAsInt() : 1 + firstSpeed + secondSpeed;
		}
		
		@Override
		public void onSubmit() {
			if(movement.getCharacter().isPlayer()) {
				Player player = movement.getCharacter().toPlayer();
				player.getActivityManager().set(ActivityManager.ActivityType.OBJECT_ACTION);
			}
			this.attach(movement.getCharacter());
			this.timer = calculateTimer();
			movement.setActive(true);
			movement.getCharacter().getMovementQueue().reset();
			if(movement.getAnimation() != null)
				movement.getCharacter().animation(movement.getAnimation());
			movement.getCharacter().getFlags().flag(UpdateFlag.FORCE_MOVEMENT);
			movement.getCharacter().getMovementQueue().setLockMovement(true);
		}
		
		@Override
		public void execute() {
			if(timer == 0) {
				movement.getCharacter().setPosition(movement.getSecond());
				movement.getCharacter().getMovementQueue().reset();
				movement.getCharacter().setUpdates(true, false);
			}
			if(timer == -1) {
				if(movement.getCharacter().isPlayer()) {
					Player player = movement.getCharacter().toPlayer();
					player.getActivityManager().remove(ActivityManager.ActivityType.OBJECT_ACTION);
					player.getForcedMovement().setActive(false);
				}
				this.cancel();
			}
			timer -= 1;
			
		}
		
		@Override
		public void onCancel() {
			if(movement != null) {
				movement.onDestination.ifPresent(Runnable::run);
				movement.getCharacter().getMovementQueue().setLockMovement(false);
			}
		}
	}
}
