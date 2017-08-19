package net.edge.net;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutException;
import net.edge.net.session.Session;

import java.nio.channels.ClosedChannelException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

/**
 * A {@link ChannelInboundHandlerAdapter} implementation that handles upstream messages from Netty.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
@Sharable
public final class EdgevilleUpstreamHandler extends ChannelInboundHandlerAdapter {
	
	/**
	 * A default access level constructor to discourage external instantiation outside of the {@code net.edge.net} package.
	 */
	EdgevilleUpstreamHandler() { }
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
		e.printStackTrace();
		Session session = getSession(ctx);
		if(session != null) {
			session.terminate();
		} else {
			ctx.close();
		}
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		Session session = getSession(ctx);
		if(session != null) {
			session.terminate();
		} else {
			ctx.close();
		}
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Session session = getSession(ctx);
		if(session != null) {
			session.handleUpstreamMessage(msg);
		} else {
			ctx.close();
		}
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws java.lang.Exception {
		Session session = getSession(ctx);
		if(session != null) {
			session.terminate();
		} else {
			ctx.close();
		}
	}
	
	/**
	 * Gets the {@link Session} instance from the {@link ChannelHandlerContext}.
	 * @param ctx The channel handler context.
	 * @return The session instance.
	 */
	private Session getSession(ChannelHandlerContext ctx) {
		return ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
	}
}