package com.rageps.net.refactor;

import com.rageps.net.refactor.codec.handshake.HandshakeDecoder;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * A {@link ChannelInitializer} for the service pipeline.
 *
 * @author Graham
 */
public final class ServiceChannelInitializer extends ChannelInitializer<SocketChannel> {

	/**
	 * The network event handler.
	 */
	private final ChannelInboundHandlerAdapter handler;

	/**
	 * Creates the service pipeline factory.
	 *
	 * @param handler The networking event handler.
	 */
	public ServiceChannelInitializer(ChannelInboundHandlerAdapter handler) {
		this.handler = handler;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("handshakeDecoder", new HandshakeDecoder());
		pipeline.addLast("timeout", new IdleStateHandler(NetworkConstants.IDLE_TIME, 0, 0));
		pipeline.addLast("handler", handler);
	}

}