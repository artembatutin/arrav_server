package net.edge.content.skill.runecrafting;

import net.edge.action.impl.ItemAction;
import net.edge.content.teleport.impl.DefaultTeleportSpell;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.locale.Position;

import static net.edge.content.teleport.impl.DefaultTeleportSpell.TeleportType.TRAINING_PORTAL;

/**
 * The enumerated type which elements represents an altar which can produce
 * magic runes.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum Altar {
	AIR(44481, Rune.AIR, 1, 7.0, new Position(2841, 4829), true, 1438),
	MIND(2479, Rune.MIND, 2, 9.0, new Position(2793, 4828), false, 1448),
	WATER(2480, Rune.WATER, 5, 12.0, new Position(3051, 4833), true, 1444),
	EARTH(2481, Rune.EARTH, 9, 15.0, new Position(2655, 4830), true, 1440),
	FIRE(2482, Rune.FIRE, 14, 18.0, new Position(2574, 4849), true, 1442),
	BODY(2483, Rune.BODY, 20, 20.0, new Position(2521, 4835), false, 1446),
	COSMIC(2484, Rune.COSMIC, 27, 50.0, new Position(2162, 4833), false, 1454),
	CHAOS(2487, Rune.CHAOS, 35, 55.0, new Position(2281, 4837), false, 1452),
	NATURE(2486, Rune.NATURE, 44, 60.0, new Position(2400, 4835), false, 1462),
	LAW(2485, Rune.LAW, 54, 75.0, new Position(2464, 4818), false, 1458),
	DEATH(2488, Rune.DEATH, 65, 80.0, new Position(2208, 4830), false, 1456),
	BLOOD(30624, Rune.BLOOD, 80, 90.0, new Position(3027, 4834), false, 1450),
	SOUL(7138, Rune.SOUL, 95, 100.0, new Position(3050, 4829), false, 1460);

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
	 * The talisman id.
	 */
	private final int talisman;

	/**
	 * Constructs an {@link Altar} enumerator.
	 * @param objectId    {@link #objectId}.
	 * @param rune        {@link #rune}.
	 * @param experience  {@link #experience}
	 * @param requirement {@link #requirement}.
	 * @param position    {@link #position}.
	 * @param diverse     {@link #diverse}.
	 * @param talisman     {@link #talisman}.
	 */
	Altar(int objectId, Rune rune, int requirement, double experience, Position position, boolean diverse, int talisman) {
		this.objectId = objectId;
		this.rune = rune;
		this.requirement = requirement;
		this.experience = experience;
		this.position = position;
		this.diverse = diverse;
		this.talisman = talisman;
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
	 * @return {@link #talisman}.
	 */
	public int getTalisman() {
		return talisman;
	}

}
