package net.edge.net.session;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;

import static com.google.common.base.Preconditions.checkState;

/**
 * An abstraction model that determines how I/O operations are handled.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class Session {
	
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
	 * Disposes of this {@code Session} by closing the {@link Channel} and executing the {@code onDispose()} listener.
	 */
	public void dispose() {
		onDispose();
	}
	
	/**
	 * Executed when this {@link Session} needs to be disposed of.
	 */
	public void onDispose() {
		
	}
	
	/**
	 * Implementations decide which messages are handled and how they are handled. Messages are ignored completely by
	 * default.
	 * @param msg The message to handle.
	 */
	public void handleUpstreamMessage(Object msg) throws Exception {
	}
	
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
}