package com.rageps.world.entity.actor.player.persist;

import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerCredentials;

/**
 * A simple interface for saving/loading players.
 *
 * Persistence of character files can and should be done on another thread
 * whenever possible to avoid doing disk/database I/O on the main game thread.
 *
 * @author Tamatea <tamateea@gmail.com>
 */
public interface PlayerPersistable {

	/**
	 * Saves the players account using the delegated method.
	 * @param player The player being saved.
	 */
	void save(Player player);

	/**
	 * Attempts to load the players account using the delegated method.
	 * @param player The player being loaded.
	 * @return A login code to return to the player.
	 */
	PlayerLoaderResponse load(PlayerCredentials player);

}
