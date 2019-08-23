package net.arrav.net;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import net.arrav.net.codec.login.LoginDecoder;

/**
 * The {@link ChannelInitializer} implementation that will initialize {@link SocketChannel}s before they are registered.
 * @author Artem Batutin
 */
public final class ArravChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	/**
	 * Handles upstream messages from Netty.
	 */
	private static final ChannelHandler HANDLER = new ArravChannelHandler();
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.attr(NetworkConstants.SESSION_KEY).setIfAbsent(new Session(ch));
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("login_decoder", new LoginDecoder());
		//pipeline.addLast("timeout", new IdleStateHandler(NetworkConstants.SESSION_TIMEOUT, 0, 0));
		pipeline.addLast("handler", HANDLER);
	}
}
