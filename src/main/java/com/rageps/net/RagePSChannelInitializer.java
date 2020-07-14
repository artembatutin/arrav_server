package com.rageps.net;

import com.rageps.net.codec.login.LoginDecoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * The {@link ChannelInitializer} implementation that will initialize {@link SocketChannel}s before they are registered.
 * @author Artem Batutin
 */
public final class RagePSChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	/**
	 * Handles upstream messages from Netty.
	 */
	private static final ChannelHandler HANDLER = new RageChannelHandler();
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.attr(NetworkConstants.SESSION_KEY).setIfAbsent(new Session(ch));
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("login_decoder", new LoginDecoder());
		//pipeline.addLast("timeout", new IdleStateHandler(NetworkConstants.SESSION_TIMEOUT, 0, 0));
		pipeline.addLast("handler", HANDLER);
	}
}
