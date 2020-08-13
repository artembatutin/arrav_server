package com.rageps.service.impl;

import com.rageps.GameConstants;
import com.rageps.net.refactor.codec.login.LoginConstants;
import com.rageps.net.refactor.codec.login.LoginRequest;
import com.rageps.net.refactor.session.impl.GameSession;
import com.rageps.net.refactor.session.impl.LoginSession;
import com.rageps.service.Service;
import com.rageps.util.ThreadUtil;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.persist.PlayerLoaderResponse;
import com.rageps.world.entity.actor.player.persist.PlayerPersistenceManager;
import com.rageps.world.login.PlayerLoaderWorker;
import com.rageps.world.login.PlayerSaverWorker;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The {@link LoginService} manages {@link LoginRequest}s.
 *
 * @author Graham
 * @author Major
 */
public final class LoginService extends Service {

	/**
	 * The World this Service is for.
	 */
	protected final World world;

	/**
	 * The {@link ExecutorService} to which workers are submitted.
	 */
	private final ExecutorService executor = Executors.newCachedThreadPool(ThreadUtil.create("LoginService"));

	/**
	 * The current {@link PlayerPersistenceManager}.
	 */
	private final PlayerPersistenceManager serializer = World.get().getPersistenceManager();

	/**
	 * Creates the login service.
	 *
	 * @param world The {@link World} to log Players in to.
	 * @throws Exception If an error occurs.
	 */
	public LoginService(World world)  {
		this.world = world;
	}

	@Override
	public void start() {
	}

	/**
	 * Submits a login request.
	 *
	 * @param session The session submitting this request.
	 * @param request The login request.
	 * @throws IOException If some I/O exception occurs.
	 */
	public void submitLoadRequest(LoginSession session, LoginRequest request) throws IOException {
		int response = LoginConstants.STATUS_OK;

		if (requiresUpdate(request)) {
			response = LoginConstants.STATUS_GAME_UPDATED;
		}

		if (response == LoginConstants.STATUS_OK) {
			executor.submit(new PlayerLoaderWorker(serializer, session, request));
		} else {
			session.handlePlayerLoaderResponse(request, new PlayerLoaderResponse(response));
		}
	}

	/**
	 * Submits a save request.
	 *
	 * @param session The session submitting this request.
	 * @param player The player to save.
	 */
	public void submitSaveRequest(GameSession session, Player player) {
		executor.submit(new PlayerSaverWorker(serializer, session, player));
	}


	/**
	 * Checks if an update is required whenever a {@link Player} submits a login request.
	 *
	 * @param request The login request.
	 * @return {@code true} if an update is required, otherwise return {@code false}.
	 * @throws IOException If some I/O exception occurs.
	 */
	private boolean requiresUpdate(LoginRequest request) throws IOException {
		return request.getClientVersion() != GameConstants.CLIENT_BUILD;
	}

}