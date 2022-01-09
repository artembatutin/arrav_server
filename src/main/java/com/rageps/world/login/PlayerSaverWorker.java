package com.rageps.world.login;


import com.rageps.net.refactor.session.impl.GameSession;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.persist.PlayerPersistenceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * A class which processes a single save request.
 *
 * @author Graham
 */
public final class PlayerSaverWorker implements Runnable {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = LogManager.getLogger();

	/**
	 * The player to save.
	 */
	private final Player player;

	/**
	 * The player saver.
	 */
	private final PlayerPersistenceManager saver;

	/**
	 * The game session.
	 */
	private final GameSession session;

	/**
	 * Creates the player saver worker.
	 *
	 * @param saver The player saver.
	 * @param session The game session.
	 * @param player The player to save.
	 */
	public PlayerSaverWorker(PlayerPersistenceManager saver, GameSession session, Player player) {
		this.saver = saver;
		this.session = session;
		this.player = player;
	}

	@Override
	public void run() {
		try {
			saver.save(player);
			session.handlePlayerSaverResponse(true);
		} catch (Exception e) {
			logger.fatal("Unable to save player's game.", e);
			session.handlePlayerSaverResponse(false);
		}
	}

}