package net.edge.net;

import com.google.common.base.Objects;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import net.edge.net.session.Session;

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
		if (NetworkConstants.IGNORED_NETWORK_EXCEPTIONS.stream().noneMatch($it -> Objects.equal($it, e.getMessage()))) {
			e.printStackTrace();
		}

		ctx.close();
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Session session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
		if (session == null) {
			throw new IllegalStateException("session == null");
		}

		session.handleUpstreamMessage(msg);
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
				ctx.channel().close();
			}
		}
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws java.lang.Exception {
		Session session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
		if (session != null) {
			session.terminate();
		}
	}
	
}