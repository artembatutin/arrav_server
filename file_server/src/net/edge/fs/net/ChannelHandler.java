package net.edge.fs.net;

import net.edge.fs.FileServer;
import net.edge.fs.FileServerConstants;
import net.edge.fs.request.Request;

import com.google.common.base.Objects;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * An {@link SimpleChannelInboundHandler} for the {@link FileServer}.
 * Manages a channel's events.
 * 
 * @author Professor Oak
 */
@Sharable
public final class ChannelHandler extends SimpleChannelInboundHandler<Request> {

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
		if (FileServerConstants.IGNORED_NETWORK_EXCEPTIONS.stream().noneMatch($it -> Objects.equal($it, e.getMessage()))) {
			e.printStackTrace();
		}
		ctx.channel().close();
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
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {
		//Dispatch the request to the client.
		request.dispatch();
	}
}
