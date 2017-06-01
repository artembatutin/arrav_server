package net.edge.content.minigame.barrows;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.locale.loc.CircleLocation;
import net.edge.locale.Position;

import java.util.EnumSet;
import java.util.Optional;

/**
 * The enumerated type whose elements represent the data required for the
 * barrows brothers.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum BarrowsData {
	AHRIM(2025, 6821, 6702, new CircleLocation(3565, 3289, 0, 5), new Position(3559, 9701, 3), new Position(3557, 9703, 3)),
	DHAROK(2026, 6771, 6703, new CircleLocation(3575, 3298, 0, 5), new Position(3552, 9716, 3), new Position(3556, 9718, 3)),
	GUTHAN(2027, 6773, 6704, new CircleLocation(3577, 3282, 0, 5), new Position(3537, 9701, 3), new Position(3534, 9704, 3)),
	KARIL(2028, 6822, 6705, new CircleLocation(3566, 3276, 0, 5), new Position(3549, 9680, 3), new Position(3546, 9684, 3)),
	TORAG(2029, 6772, 6706, new CircleLocation(3554, 3283, 0, 5), new Position(3572, 9687, 3), new Position(3568, 9683, 3)),
	VERAC(2030, 6823, 6707, new CircleLocation(3557, 3297, 0, 5), new Position(3575, 9705, 3), new Position(3578, 9706, 3));
	
	/**
	 * Caches our enum values.
	 */
	static final ImmutableSet<BarrowsData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(BarrowsData.class));
	
	/**
	 * The npc identification of this brother.
	 */
	private final int npcId;
	
	/**
	 * The sarcophagus identification of this brother.
	 */
	private final int sarcophagusId;
	
	/**
	 * The stair id of this cave.
	 */
	private final int stairId;
	
	/**
	 * The circle location of the spot to dig.
	 */
	private final CircleLocation location;
	
	/**
	 * The spawn position of this barrow brother.
	 */
	private final Position spawn;
	
	/**
	 * The cave positions to move the player to upon digging.
	 */
	private final Position cave;
	
	/**
	 * Constructs a new {@link BarrowsData} enumerator.
	 * @param npcId         {@link #npcId}.
	 * @param sarcophagusId {@link #sarcophagusId}.
	 * @param stairId       {@link #stairId}.
	 * @param location      {@link #location}.
	 * @param cave          {@link #cave}.
	 */
	BarrowsData(int npcId, int sarcophagusId, int stairId, CircleLocation location, Position spawn, Position cave) {
		this.npcId = npcId;
		this.sarcophagusId = sarcophagusId;
		this.stairId = stairId;
		this.location = location;
		this.spawn = spawn;
		this.cave = cave;
	}
	
	/**
	 * @return the npcId
	 */
	public int getNpcId() {
		return npcId;
	}
	
	/**
	 * @return the sarcophagusId
	 */
	public int getSarcophagusId() {
		return sarcophagusId;
	}
	
	/**
	 * @return the stairId
	 */
	public int getStairId() {
		return stairId;
	}
	
	/**
	 * @return the spawn
	 */
	public Position getSpawn() {
		return spawn;
	}
	
	/**
	 * @return the location
	 */
	public CircleLocation getLocation() {
		return location;
	}
	
	/**
	 * Attempts to find the barrows data dependent on the spot the player digs
	 * at.
	 * @param position the position to check for.
	 * @return the barrows data wrapped in an optional, {@link Optional#empty()} otherwise.
	 */
	protected static Optional<BarrowsData> getDefinitionForLocation(Position position) {
		return VALUES.stream().filter(def -> def.location.inLocation(position)).findFirst();
	}
	
	/**
	 * Attempts to find the barrows data dependent on the cave the player is
	 * in at.
	 * @param position the position to check for.
	 * @return the barrows data wrapped in an optional, {@link Optional#empty()} otherwise.
	 */
	protected static Optional<BarrowsData> getDefinitionForCave(Position position) {
		return VALUES.stream().filter(def -> position.withinDistance(def.cave, 20)).findFirst();
	}
}