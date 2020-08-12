package com.rageps;


import com.rageps.service.impl.GameService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * A class which handles the logic for each pulse of the {@link GameService}.
 *
 * @author Graham
 */
public final class GamePulseHandler implements Runnable {

	/**
	 * The Logger for this class.
	 */
	private static final Logger logger = LogManager.getLogger();

	/**
	 * The {@link GameService}.
	 */
	private final GameService service;

	/**
	 * Creates the GamePulseHandler.
	 *
	 * @param service The {@link GameService}.
	 */
	public GamePulseHandler(GameService service) {
		this.service = service;
	}

	@Override
	public void run() {
		try {
			service.pulse();
		} catch (Throwable throwable) {
			logger.fatal("Exception occured during pulse!", throwable);
		}
	}

}