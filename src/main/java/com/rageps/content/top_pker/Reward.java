package com.rageps.content.top_pker;


import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

import java.time.ZonedDateTime;

/**
 * A reward for a player with a given action that would append the reward
 * to that player.
 */
interface Reward {

	/**
	 * Appends the reward to the player in some way.
	 *
	 * @param player the player receiving the reward.
	 */
	void append(Player player);

	/**
	 * Gets the Item for this Reward.
	 *
	 * @return The Item for this Reward.
	 */
	Item getItem();

	/**
	 * Determines whether or not the reward itself can be appended to the player based on some factors.
	 *
	 * @param player
	 * 			  the player we're checking it it's appendable or not.
	 * @param endDate
	 * 			  the end date of the session for the reward.
	 * @return true if the reward can be appended.
	 */
	default boolean appendable(Player player, ZonedDateTime endDate) {
		return true;
	}

}
