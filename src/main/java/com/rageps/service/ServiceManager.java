package com.rageps.service;


import com.rageps.service.impl.GameService;
import com.rageps.service.impl.LoginService;
import com.rageps.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A class which manages {@link Service}s.
 *
 * @author Graham
 * @author Major
 */
public final class ServiceManager {

	/**
	 * The Logger for this class.
	 */
	private static final Logger logger = LogManager.getLogger();

	/**
	 * The GameService.
	 */
	private final GameService game;

	/**
	 * The LoginService.
	 */
	private final LoginService login;


	/**
	 * Creates and initializes the {@link ServiceManager}.
	 *
	 * @param world The {@link World} to create the {@link Service}s for.
	 * @throws Exception If there is an error creating the Services.
	 */
	public ServiceManager(World world) {
			game = new GameService(world);
		System.out.println(world == null);
		login = new LoginService(world);
	}

	/**
	 * Gets the {@link GameService}.
	 *
	 * @return The GameService.
	 */
	public GameService getGame() {
		return game;
	}

	/**
	 * Gets the {@link LoginService}.
	 *
	 * @return The LoginService.
	 */
	public LoginService getLogin() {
		return login;
	}

	/**
	 * Starts all the services.
	 */
	public void startAll() {
		logger.info("Starting services...");
		game.start();
		login.start();
	}

}