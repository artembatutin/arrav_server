package net.edge.content.skill.summoning.familiar.passive;

import net.edge.world.entity.actor.player.Player;

/**
 * The passive ability indicates functionality which will be handled
 * automatically as soon as the player summons the familiar.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public interface PassiveAbility {

	/**
	 * Checks if the ability can be executed.
	 */
	boolean canExecute(Player player);

	/**
	 * The functionality executed for this ability.
	 */
	void onExecute(Player player);

	/**
	 * The type of this ability.
	 *
	 * @return the type of this ability.
	 */
	PassiveAbilityType getPassiveAbilityType();

	/**
	 * The possible ability types linked to this passive ability.
	 *
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public enum PassiveAbilityType {
		PERIODICAL;
	}

}
