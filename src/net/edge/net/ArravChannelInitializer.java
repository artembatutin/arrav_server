package net.edge.net;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * The {@link ChannelInitializer} implementation that will initialize {@link SocketChannel}s before they are registered.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class ArravChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	/**
	 * Handles upstream messages from Netty.
	 */
	private static final ChannelHandler HANDLER = new ArravChannelHandler();
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.attr(NetworkConstants.SESSION_KEY).setIfAbsent(new Session(ch));
		ch.pipeline().addLast("upstream-handler", HANDLER);
	}
}
