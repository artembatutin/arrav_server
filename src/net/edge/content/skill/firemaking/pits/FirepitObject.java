package net.edge.content.skill.firemaking.pits;

import net.edge.util.TextUtils;
import net.edge.content.skill.firemaking.LogType;
import net.edge.locale.Position;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.DynamicObject;
import net.edge.world.object.ObjectDirection;
import net.edge.world.object.ObjectType;

import java.util.Optional;

/**
 * Represents a single fire pit object.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class FirepitObject extends DynamicObject {

	/**
	 * The data for this fire pit object.
	 */
	FirepitData data;

	/**
	 * Determines if this fire pit is active.
	 */
	Optional<FirepitTask> active;

	/**
	 * Constructs a new {@link FirepitObject}.
	 */
	FirepitObject() {
		super(FirepitData.PHASE_ONE.objectId, new Position(3081, 3497), ObjectDirection.SOUTH, ObjectType.GENERAL_PROP, false, 0, 0);
		this.data = FirepitData.PHASE_ONE;
		this.active = Optional.empty();//never active when constructed.
	}

	public boolean isActive() {
		return active.isPresent();
	}
	
	public Optional<FirepitTask> getTask() {
		return active;
	}

	public int getTime() {
		return (FirepitManager.EVENT_TIME_IN_TICKS - active.get().getCounter()) * 600;
	}
	
	public void setActive(Optional<FirepitTask> active) {
		this.active = active;
	}

	public void fire(Player player) {
		PitFiring firemaking = new PitFiring(player, this);
		firemaking.start();
	}

	/**
	 * Increments the amount of logs this fire pit has.
	 */
	public void increment() {
		setElements(getElements() + 1);
		if(getElements() >= data.count && this.data.getNext().isPresent()) {
			this.data = this.data.getNext().get();
			this.setId(data.objectId);
			this.publish();
		}
	}
	
	/**
	 * Gets the log requirement into a string.
	 * @return the string which identifies the log requirement.
	 */
	public String getLogRequirement() {
		return TextUtils.capitalize(data.log.toString());
	}

	/**
	 * Determines if the specified {@code log} can be added to the fire pit.
	 * @param player the player attempting to add the log.
	 * @param log    the log that was added.
	 * @return {@code true} if the log is permissible, {@code false} otherwise.
	 */
	public boolean isPermissable(Player player, int log) {
		LogType type = LogType.getDefinition(log).orElse(null);

		if(type == null) {
			player.message("You can only add logs to this fire pit.");
			return false;
		}
		
		if(this.data.log == null) {
			player.message("Let the pit burn. Don't add any logs yet.");
			return false;
		}

		if(getElements() >= data.count && !this.data.getNext().isPresent() && this.data.equals(FirepitData.PHASE_FIVE)) {
			player.message("You can't add logs anymore... You have to fire the pile of logs.");
			return false;
		}

		if(type.ordinal() < this.data.log.ordinal()) {
			player.message("Your log does not seem right for this fire pit...");
			return false;
		}
		return true;
	}

}
