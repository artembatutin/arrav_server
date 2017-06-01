package net.edge.content.skill.runecrafting;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.edge.locale.Position;

import java.util.EnumSet;
import java.util.Optional;

/**
 * The enumerated type which elements represents an altar which can produce
 * magic runes.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum Altar {
	AIR(44481, Rune.AIR, 1, 7.0, new Position(2841, 4829), true),
	MIND(2479, Rune.MIND, 2, 9.0, new Position(2793, 4828), false),
	WATER(2480, Rune.WATER, 5, 12.0, new Position(2726, 4832), true),
	EARTH(2481, Rune.EARTH, 9, 15.0, new Position(2655, 4830), true),
	FIRE(2482, Rune.FIRE, 14, 18.0, new Position(2574, 4849), true),
	BODY(2483, Rune.BODY, 20, 20.0, new Position(2523, 4826), false),
	COSMIC(2484, Rune.COSMIC, 27, 50.0, new Position(2162, 4833), false),
	CHAOS(2487, Rune.CHAOS, 35, 55.0, new Position(2281, 4837), false),
	NATURE(2486, Rune.NATURE, 44, 60.0, new Position(2400, 4835), false),
	LAW(2485, Rune.LAW, 54, 75.0, new Position(2464, 4818), false),
	DEATH(2488, Rune.DEATH, 65, 80.0, new Position(2208, 4830), false),
	BLOOD(30624, Rune.BLOOD, 80, 90.0, new Position(3027, 4834), false),
	SOUL(7138, Rune.SOUL, 95, 100.0, new Position(3050, 4829), false);

	/**
	 * Caches our enum values.
	 */
	private static final ImmutableSet<Altar> VALUES = Sets.immutableEnumSet(EnumSet.allOf(Altar.class));

	/**
	 * The object identification for this altar.
	 */
	private final int objectId;

	/**
	 * The {@link Rune} this altar produces.
	 */
	private final Rune rune;

	/**
	 * The requirement for entering this altar.
	 */
	private final int requirement;

	/**
	 * The experience identifier for this altar.
	 */
	private final double experience;

	/**
	 * The position the player will be moved to upon entering.
	 */
	private final Position position;

	/**
	 * Determines if this altar can accept both runes.
	 */
	private final boolean diverse;

	/**
	 * Constructs an {@link Altar} enumerator.
	 * @param objectId    {@link #objectId}.
	 * @param rune        {@link #rune}.
	 * @param experience  {@link #experience}
	 * @param requirement {@link #requirement}.
	 * @param position    {@link #position}.
	 * @param diverse     {@link #diverse}.
	 */
	Altar(int objectId, Rune rune, int requirement, double experience, Position position, boolean diverse) {
		this.objectId = objectId;
		this.rune = rune;
		this.requirement = requirement;
		this.experience = experience;
		this.position = position;
		this.diverse = diverse;
	}

	/**
	 * @return {@link #objectId}.
	 */
	public int getObjectId() {
		return objectId;
	}

	/**
	 * @return {@link #rune}.
	 */
	public Rune getRune() {
		return rune;
	}

	/**
	 * @return {@link #requirement}.
	 */
	public int getRequirement() {
		return requirement;
	}

	/**
	 * @return {@link #experience}.
	 */
	public double getExperience() {
		return experience;
	}

	/**
	 * @return {@link #position}.
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * @return {@link #diverse}.
	 */
	public boolean isDiverse() {
		return diverse;
	}

	/**
	 * Gets the definition for this altar.
	 * @param identifier the identifier to check for.
	 * @return an optional holding the {@link Altar} value found,
	 * {@link Optional#empty} otherwise.
	 */
	public static Optional<Altar> getDefinition(int identifier) {
		return VALUES.stream().filter(def -> def.objectId == identifier).findAny();
	}
}
