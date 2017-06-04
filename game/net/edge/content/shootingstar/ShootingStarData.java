package net.edge.content.shootingstar;

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
	PHASE_NINE(9, 38660, 10, 15, 130),
	PHASE_EIGHT(8, 38661, 20, 25, 106),
	PHASE_SEVEN(7, 38662, 30, 40, 87),
	PHASE_SIX(6, 38663, 40, 80, 61),
	PHASE_FIVE(5, 38664, 50, 175, 44),
	PHASE_FOUR(4, 38665, 60, 250, 34),
	PHASE_THREE(3, 38666, 70, 439, 25),
	PHASE_TWO(2, 38667, 80, 700, 20),
	PHASE_ONE(1, 38668, 90, 1200, 14);
	
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
	 * @param size             {@link #size}.
	 * @param objectId         {@link #objectId}.
	 * @param levelRequirement {@link #log}.
	 * @param stardust         {@link #stardust}.
	 * @param experience       {@link #experience}.
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
