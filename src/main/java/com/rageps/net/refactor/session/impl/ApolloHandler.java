package com.rageps.net.refactor.session.impl;

import com.google.common.base.Objects;
import com.rageps.net.NetworkConstants;
import com.rageps.net.refactor.codec.handshake.HandshakeConstants;
import com.rageps.net.refactor.codec.handshake.HandshakeMessage;
import com.rageps.net.refactor.session.Session;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An implementation of {@link ChannelInboundHandlerAdapter} which handles incoming upstream events from Netty.
 *
 * @author Graham
 */
@Sharable
public final class ApolloHandler extends ChannelInboundHandlerAdapter {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = LogManager.getLogger();

	/**
	 * The {@link Session} {@link AttributeKey}.
	 */
	public static final AttributeKey<Session> SESSION_KEY = AttributeKey.valueOf("session");


	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		Channel channel = ctx.channel();
		Session session = channel.attr(ApolloHandler.SESSION_KEY).getAndSet(null);
		if (session != null) {
			session.destroy();
		}
		logger.info("Channel disconnected: {}", channel);
		channel.close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
		if(NetworkConstants.IGNORED_NETWORK_EXCEPTIONS.stream().noneMatch($it -> Objects.equal($it, e.getMessage()))) {
			logger.warn("Exception occured for channel: {}, closing...",ctx.channel(), e);
		}
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object message) throws Exception {
		Channel channel = ctx.channel();
		Attribute<Session> attribute = channel.attr(ApolloHandler.SESSION_KEY);
		Session session = attribute.get();

		//if (message instanceof HttpRequest || message instanceof JagGrabRequest) {
		//	session = new UpdateSession(channel, serverContext);
		//}

		if (session != null) {
			session.messageReceived(message);
			return;
		} else {
			System.out.println("session is null!! "+ctx.channel().remoteAddress());
		}

		// TODO: Perhaps let HandshakeMessage implement Message to remove this explicit check
		if (message instanceof HandshakeMessage) {
			HandshakeMessage handshakeMessage = (HandshakeMessage) message;

			switch (handshakeMessage.getServiceId()) {
				case HandshakeConstants.SERVICE_GAME:
					attribute.set(new LoginSession(channel));
					break;

				//case HandshakeConstants.SERVICE_UPDATE:
				//	attribute.set(new UpdateSession(channel, serverContext));
				//	break;
			}
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

}