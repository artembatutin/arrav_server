package net.edge.world.content.skill.firemaking.pits;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.world.content.skill.firemaking.LogType;

import java.util.EnumSet;
import java.util.Optional;

/**
 * The enumerated type whose elements represent a set of constants used
 * for the fire pits.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum FirepitData {
	PHASE_ONE(38817, LogType.LOG, 500),
	PHASE_TWO(38818, LogType.OAK, 600),
	PHASE_THREE(38819, LogType.WILLOW, 700),
	PHASE_FOUR(38820, LogType.YEW, 850),
	PHASE_FIVE(38821, LogType.MAGIC, 1000),
	PHASE_IGNITED(38828, null, -1);

	/**
	 * Caches our enum values.
	 */
	public static final ImmutableSet<FirepitData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(FirepitData.class));

	/**
	 * The object id of this fire pit.
	 */
	final int objectId;

	/**
	 * The minimum log type which can be sacrificed.
	 */
	final LogType log;

	/**
	 * The amount of logs required to move to the next phase.
	 */
	final int count;

	/**
	 * Constructs a new {@link FirepitData}.
	 * @param objectId {@link #objectId}.
	 * @param log      {@link #log}.
	 * @param count    {@link #count}.
	 */
	FirepitData(int objectId, LogType log, int count) {
		this.objectId = objectId;
		this.log = log;
		this.count = count;
	}

	static Optional<FirepitData> valueOf(int index) {
		return VALUES.stream().filter(fire -> fire.ordinal() == index).findAny();
	}

	Optional<FirepitData> getNext() {
		return valueOf(this.ordinal() + 1);
	}

	public int getObjectId() {
		return objectId;
	}
}
