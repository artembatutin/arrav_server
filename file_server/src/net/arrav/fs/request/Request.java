package net.arrav.fs.request;

import io.netty.channel.Channel;

/**
 * Represents some type of request sent from the client.
 * @author Professor Oak
 */
public abstract class Request {

	/**
	 * Creates this request.
	 * @param channel	The channel that made this request.
	 */
	public Request(Channel channel) {
		this.channel = channel;
	}
	
	/**
	 * The channel of this request.
	 */
	private final Channel channel;
	
	/**
	 * Gets the channel.
	 * @return
	 */
	public Channel getChannel() {
		return channel;
	}
		
	/**
	 * Sends this request to the client
	 */
	public abstract void dispatch();
}
