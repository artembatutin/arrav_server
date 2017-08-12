package net.edge.content.object.star;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.world.locale.Position;

import java.util.EnumSet;

/**
 * The enumerated type whose elements represent a set of constants used to define
 * the locations the shooting stars land on.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum StarLocationData {
	EDGEVILLE_HOME_LOCATION(3101, 3508, "A shooting star has just struck east from Edgeville's bank!", "The star east from Edgeville's bank hasn't been mined yet!"),
	LUMBRIDGE_BRIDGE_LOCATION(3234, 3224, "A shooting star has just struck near the Lumbridge's bridge!", "The star near the Lumbridge's bridge hasn't been mined yet!"),
	ROCK_CRABS_LOCATION(2660, 3708, "A shooting star has just struck near the rock crabs!", "The star near the Rock crabs hasn't been mined yet!"),
	DUEL_ARENA_LOCATION(3368, 3265, "A shooting star has just struck near the duel arena!", "The star near the Duelling Arena hasn't been mined yet!");
	
	/**
	 * Caches our enum values.
	 */
	static final ImmutableSet<StarLocationData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(StarLocationData.class));
	
	/**
	 * The position to spawn this star at.
	 */
	final Position position;
	
	/**
	 * The message to send to the world.
	 */
	final String message;
	
	/**
	 * The message to send to the world if this star hasn't been mined fully.
	 */
	final String messageActive;
	
	/**
	 * Constructs a new {@link StarLocationData}.
	 * @param x             the x-tile to spawn this star on.
	 * @param y             the y-tile to spawn this star on.
	 * @param message       the message to send to the world.
	 * @param messageActive the message to send to the world if this star hasn't been mined fully.
	 */
	StarLocationData(int x, int y, String message, String messageActive) {
		this.position = new Position(x, y);
		this.message = message;
		this.messageActive = messageActive;
	}

	/**
	 * Constructs a new {@link StarLocationData}.
	 * @param x             the x-tile to spawn this star on.
	 * @param y             the y-tile to spawn this star on.
	 * @param z             the z-tile to spawn this star on.
	 * @param message       the message to send to the world.
	 * @param messageActive the message to send to the world if this star hasn't been mined fully.
	 */
	StarLocationData(int x, int y, int z, String message, String messageActive) {
		this.position = new Position(x, y, z);
		this.message = message;
		this.messageActive = messageActive;
	}
	
	public String getMessageWhenActive() {
		return messageActive;
	}
}
