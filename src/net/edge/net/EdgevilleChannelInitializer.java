package net.edge.net;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.edge.net.codec.login.LoginDecoder;
import net.edge.net.codec.login.LoginEncoder;
import net.edge.net.session.LoginSession;
import net.edge.net.session.Session;

/**
 * The {@link ChannelInitializer} implementation that will initialize {@link SocketChannel}s before they are registered.
 * @author lare96 <http://github.com/lare96>
 */
public final class EdgevilleChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	/**
	 * Handles upstream messages from Netty.
	 */
	private final ChannelHandler upstreamHandler = new EdgevilleUpstreamHandler();
	
	/**
	 * Filters channels based on the amount of active connections they have.
	 */
	private final ChannelHandler channelFilter = new EdgevilleChannelFilter();
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.attr(NetworkConstants.SESSION_KEY).setIfAbsent(new LoginSession(ch));
		ch.pipeline().addLast("read-timeout", new ReadTimeoutHandler(NetworkConstants.INPUT_TIMEOUT));
		ch.pipeline().addLast("channel-filter", channelFilter);
		ch.pipeline().addLast("login-decoder", new LoginDecoder());
		ch.pipeline().addLast("login-encoder", new LoginEncoder());
		ch.pipeline().addLast("upstream-handler", upstreamHandler);
	}
}
