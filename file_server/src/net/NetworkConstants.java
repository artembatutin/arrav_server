package net;

/**
 * A class which holds net-related constants.
 * @author Graham
 */
public final class NetworkConstants {
	
	/**
	 * The HTTP port.
	 */
	
	/**
	 * The JAGGRAB port.
	 */
	public static final int JAGGRAB_PORT = 43596;
	
	/**
	 * The service port (which is also used for the 'on-demand' protocol).
	 */
	public static final int SERVICE_PORT = 43595;

	/**
	 * The number of seconds a channel can be idle before being closed
	 * automatically.
	 */
	public static final int IDLE_TIME = 15;
	
	/**
	 * Default private constructor to prevent instantiaton.
	 */
	private NetworkConstants() {
		
	}

}
