package net.edge.net.session;

import io.netty.channel.Channel;
import net.edge.world.entity.actor.player.Player;

import java.net.InetSocketAddress;

import static com.google.common.base.Preconditions.checkState;

/**
 * An abstraction model that determines how I/O operations are handled.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public abstract class Session {
	
	/**
	 * The {@link Channel} to send and receive messages through.
	 */
	private final Channel channel;
	
	/**
	 * The address that the connection was received from.
	 */
	private final String hostAddress;
	
	/**
	 * Condition if this session is active.
	 */
	private boolean active = true;
	
	/**
	 * Condition if this session is terminating.
	 */
	private boolean terminating = false;
	
	/**
	 * Creates a new {@link Session}.
	 * @param channel The {@link Channel} to send and receive messages through.
	 */
	public Session(Channel channel) {
		this.channel = channel;
		if(channel == null)
			this.hostAddress = "";
		else
			this.hostAddress = ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
	}
	
	/**
	 * Implementations decide which messages are handled and how they are handled. Messages are ignored completely by default.
	 * @param msg The message to handle.
	 */
	public abstract void handleUpstreamMessage(Object msg) throws Exception;
	
	/**
	 * Disposes of this {@code Session} by closing the {@link Channel}.
	 */
	public abstract void terminate();
	
	/**
	 * Gets the player associated with this {@link Session} if possible.
	 * @return player in this session.
	 */
	public abstract Player getPlayer();
	
	/**
	 * @return The {@link Channel} to send and receive messages through.
	 */
	public final Channel getChannel() {
		return channel;
	}
	
	/**
	 * @return The address that the connection was received from.
	 */
	public final String getHost() {
		return hostAddress;
	}
	
	/**
	 * @return Active connection condition.
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * Setting the connection condition.
	 * @param active flag.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	/**
	 * @return Terminating connection condition.
	 */
	public boolean isTerminating() {
		return terminating;
	}
	
	/**
	 * Setting the terminating condition.
	 * @param terminating flag.
	 */
	public void setTerminating(boolean terminating) {
		this.terminating = terminating;
	}
}