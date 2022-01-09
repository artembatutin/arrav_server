package com.rageps.world.login;


import com.rageps.net.refactor.codec.login.LoginConstants;
import com.rageps.net.refactor.codec.login.LoginRequest;
import com.rageps.net.refactor.session.impl.LoginSession;
import com.rageps.world.entity.actor.player.persist.PlayerLoaderResponse;
import com.rageps.world.entity.actor.player.persist.PlayerPersistenceManager;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class which processes a single login request.
 *
 * @author Graham
 */
public final class PlayerLoaderWorker implements Runnable {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(PlayerLoaderWorker.class.getName());

	/**
	 * The PlayerSerializer.
	 */
	private final PlayerPersistenceManager loader;

	/**
	 * The request.
	 */
	private final LoginRequest request;

	/**
	 * The session that submitted the request.
	 */
	private final LoginSession session;

	/**
	 * Creates a {@link PlayerLoaderWorker} which will do the work for a single player load request.
	 *
	 * @param loader The {@link PlayerPersistenceManager}.
	 * @param session The {@link LoginSession} which initiated the request.
	 * @param request The {@link LoginRequest} object.
	 */
	public PlayerLoaderWorker(PlayerPersistenceManager loader, LoginSession session, LoginRequest request) {
		this.loader = loader;
		this.session = session;
		this.request = request;
	}

	@Override
	public void run() {
		try {
			PlayerLoaderResponse response = loader.load(request.getCredentials());
			session.handlePlayerLoaderResponse(request, response);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unable to load player's game.", e);
			session.handlePlayerLoaderResponse(request, new PlayerLoaderResponse(LoginConstants.STATUS_COULD_NOT_COMPLETE));
		}
	}

}