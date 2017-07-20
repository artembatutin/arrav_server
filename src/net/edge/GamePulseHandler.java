package net.edge;

import net.edge.world.World;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class which handles the logic for each pulse of the {@link World}.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class GamePulseHandler implements Runnable {
	
	/**
	 * The Logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(GamePulseHandler.class.getName());
	
	/**
	 * The {@link World}.
	 */
	private final World world;
	
	/**
	 * Creates the GamePulseHandler.
	 * @param service The {@link World}.
	 */
	public GamePulseHandler(World service) {
		this.world = service;
	}
	
	@Override
	public void run() {
		try {
			world.pulse();
		} catch (Throwable throwable) {
			logger.log(Level.SEVERE, "Exception occurred during pulse!", throwable);
		}
	}
}
