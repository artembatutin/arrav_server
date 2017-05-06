package net.edge;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Instantiates a {@link Server} that will start this application.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class Main {
	
	/**
	 * The asynchronous logger.
	 */
	private static final Logger LOGGER = Logger.getLogger("Edgeville");
	
	/**
	 * A private constructor to discourage external instantiation.
	 */
	private Main() {
	}
	
	static {
		try {
			Thread.currentThread().setName("EdgevilleInitializationThread");
		} catch(Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	/**
	 * Invoked when this program is started, initializes the {@link Server}.
	 * @param args The runtime arguments, none of which are parsed.
	 */
	public static void main(String[] args) {
		boolean online = Boolean.parseBoolean(args[0]);
		if(online)
			Runtime.getRuntime().addShutdownHook(new ServerHook());
		try {
			Server edgeville = new Server(online);
			edgeville.init();
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Error in game run time!", e);
			System.exit(0);
		}
	}
}