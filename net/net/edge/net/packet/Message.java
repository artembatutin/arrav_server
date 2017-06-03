package net.edge.net.packet;

/**
 * A class which respresnts the outcome of a {@link PacketReader}.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class Message {
	
	/**
	 * Indicates whether or not the Message chain has been terminated.
	 */
	private boolean terminated;
	
	/**
	 * Terminates the Message chain.
	 */
	public final void terminate() {
		terminated = true;
	}
	
	/**
	 * Returns whether or not the Message chain has been terminated.
	 *
	 * @return {@code true} if the Message chain has been terminated, otherwise {@code false}.
	 */
	public final boolean terminated() {
		return terminated;
	}

}
