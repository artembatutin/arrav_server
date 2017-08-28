package net.edge.content.object.star;

import net.edge.content.dialogue.impl.StatementDialogue;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.object.DynamicObject;
import net.edge.world.object.ObjectDirection;
import net.edge.world.object.ObjectType;

/**
 * Represents a single shooting star object.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class ShootingStar extends DynamicObject {

	/**
	 * The data for this shooting star.
	 */
	ShootingStarData data;

	/**
	 * The location data for this shooting star.
	 */
	StarLocationData locationData;

	/**
	 * The star sprite for this shooting star.
	 */
	final StarSprite sprite = new StarSprite(this);

	/**
	 * The player who found the star at first to give double experience to.
	 */
	String username = "";

	/**
	 * Constructs a new {@link ShootingStar}.
	 *
	 * @param locationData the data to construct this star from.
	 */
	public ShootingStar(StarLocationData locationData) {
		super(ShootingStarData.PHASE_NINE.objectId, locationData.position.copy(), ObjectDirection.SOUTH, ObjectType.GENERAL_PROP, false, 0, 0);
		this.data = ShootingStarData.PHASE_NINE;
		this.locationData = locationData;
	}

	/**
	 * Attempts to mine this star.
	 *
	 * @param player   the player attempting to mine the star.
	 * @param objectId the object id interacted with.
	 * @return {@code true} if the player mined, {@code false} otherwise.
	 */
	public boolean mine(Player player, int objectId) {
		if(this.getId() != objectId) {
			return false;
		}

		StarMining.mine(player, this);
		return true;
	}

	/**
	 * Attempts to prospect this star.
	 *
	 * @param player   the player attempting to prospect the star.
	 * @param objectId the object id interacted with.
	 * @return {@code true} if the player prospected, {@code false} otherwise.
	 */
	public boolean prospect(Player player, int objectId) {
		if(this.getId() != objectId) {
			return false;
		}
		int percent = (int) ((float) getElements() / data.stardust * 100);
		player.getDialogueBuilder().append(new StatementDialogue("This is a size-" + this.data.size + " star. A mining level of at least " + this.data.levelRequirement + " is required to", "mine this layer. It has been mined about " + percent + " percent of the way", "to the next layer."));
		return true;
	}

	public int requirement() {
		return data.levelRequirement;
	}

	public StarSprite getStarSprite() {
		return sprite;
	}

	public StarLocationData getLocationData() {
		return locationData;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
