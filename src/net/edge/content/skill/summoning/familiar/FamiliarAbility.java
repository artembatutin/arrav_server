package net.edge.content.skill.summoning.familiar;

import net.edge.world.entity.actor.player.Player;

/**
 * The abstract class which holds basic functionality for familiar abilities.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class FamiliarAbility {
	
	/**
	 * The ability type of this familiar.
	 */
	private final FamiliarAbilityType type;
	
	/**
	 * Constructs a new {@link FamiliarAbility}.
	 * @param type {@link #type}.
	 */
	public FamiliarAbility(FamiliarAbilityType type) {
		this.type = type;
	}
	
	/**
	 * Initializes the familiars ability as soon as it is summoned.
	 * @param player the player we're activating this functionality for.
	 */
	public abstract void initialise(Player player);
	
	/**
	 * @return {@link #getType()}.
	 */
	public final FamiliarAbilityType getType() {
		return type;
	}
	
	/**
	 * The flag which identifies if this ability can hold items.
	 * @return <true> if the familiar's ability can hold items, <false> otherwise.
	 */
	public boolean isHoldableContainer() {
		return false;
	}
	
	/**
	 * The enumerated type whose elements represent the familiar ability types
	 * a player can summon.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public enum FamiliarAbilityType {
		FIGHTER(),
		LIGHT_ENHANCER(),
		SKILL_BOOSTER(),
		REMOTE_VIEW(),
		BEAST_OF_BURDEN(),
		HEALER(),
		FORAGER(),
		TELEPORTER();
	}
}
