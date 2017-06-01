package net.edge.content.skill.firemaking;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.EnumSet;
import java.util.Optional;

/**
 * The enumerated type whose elements represent the data for fire lighters..
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum FireLighter {
	TINDERBOX(590, 2732),
	RED_FIRELIGHTER(7329, 11404),
	GREEN_FIRELIGHTER(7330, 11405),
	WHITE_FIRELIGHTER(10327, 20000),
	PURPLE_FIRELIGHTER(10326, 20001),
	BLUE_FIRELIGHTER(7331, 11406);
	
	/**
	 * Caches our enum values.
	 */
	public static final ImmutableSet<FireLighter> VALUES = Sets.immutableEnumSet(EnumSet.allOf(FireLighter.class));
	
	/**
	 * The identification for this fire lighter.
	 */
	private final int item;
	
	/**
	 * The identification for this object id.
	 */
	private final int objectId;
	
	/**
	 * Constructs a new {@link FireLighter}.
	 * @param itemId   {@link #item}.
	 * @param objectId {@link #objectId}.
	 */
	FireLighter(int itemId, int objectId) {
		this.item = itemId;
		this.objectId = objectId;
	}
	
	public static Optional<FireLighter> getDefinition(int id) {
		return VALUES.stream().filter(def -> def.item == id).findAny();
	}
	
	/**
	 * Gets the definition for this fire lighter.
	 * @param id       the identifier to check for matches.
	 * @param secondId the second identifier to check for matches.
	 * @return an Optional with the found value, {@link Optional#empty} otherwise.
	 */
	protected static Optional<FireLighter> getDefinition(int id, int secondId) {
		return VALUES.stream().filter(def -> def.item == id || def.item == secondId).findAny();
	}
	
	/**
	 * @return {@link #item}.
	 */
	public int getItem() {
		return item;
	}
	
	/**
	 * @return {@link #objectId}.
	 */
	public int getObjectId() {
		return objectId;
	}
}
