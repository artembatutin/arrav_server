package com.rageps.content.object.star;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.EnumSet;
import java.util.Optional;

/**
 * The enumerated type whose elements represent a set of constants used
 * for the shooting stars.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum ShootingStarData {
	PHASE_NINE(9, 80663, 10, 15, 32),
	PHASE_EIGHT(8, 80664, 20, 25, 26),
	PHASE_SEVEN(7, 80665, 30, 40, 21),
	PHASE_SIX(6, 80666, 40, 80, 15),
	PHASE_FIVE(5, 80667, 50, 175, 11),
	PHASE_FOUR(4, 80668, 60, 250, 8),
	PHASE_THREE(3, 80669, 70, 439, 6),
	PHASE_TWO(2, 80670, 80, 700, 5),
	PHASE_ONE(1, 80671, 90, 1200, 3);
	
	/**
	 * Caches our enum values.
	 */
	public static final ImmutableSet<ShootingStarData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(ShootingStarData.class));
	
	/**
	 * The size of this star.
	 */
	final int size;
	
	/**
	 * The object id of this fire pit.
	 */
	final int objectId;
	
	/**
	 * The level required to mine this star.
	 */
	final int levelRequirement;
	
	/**
	 * The amount of stardust required to move to the next phase.
	 */
	final int stardust;
	
	/**
	 * The amount of experience gained per star dust.
	 */
	final int experience;
	
	/**
	 * Constructs a new {@link ShootingStarData}.
	 * @param size {@link #size}.
	 * @param objectId {@link #objectId}.
	 * @param levelRequirement {@link #levelRequirement}.
	 * @param stardust {@link #stardust}.
	 * @param experience {@link #experience}.
	 */
	ShootingStarData(int size, int objectId, int levelRequirement, int stardust, int experience) {
		this.size = size;
		this.objectId = objectId;
		this.levelRequirement = levelRequirement;
		this.stardust = stardust;
		this.experience = experience;
	}
	
	static Optional<ShootingStarData> valueOf(int index) {
		return VALUES.stream().filter(star -> star.ordinal() == index).findAny();
	}
	
	Optional<ShootingStarData> getNext() {
		return valueOf(this.ordinal() + 1);
	}
	
	public int getObjectId() {
		return objectId;
	}
}
