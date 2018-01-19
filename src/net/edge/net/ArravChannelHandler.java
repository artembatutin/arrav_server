package net.edge.net;

import com.google.common.base.Objects;
import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import net.edge.Application;
import net.edge.content.commands.impl.UpdateCommand;
import net.edge.net.codec.login.LoginCode;
import net.edge.net.host.HostListType;
import net.edge.net.host.HostManager;

import java.net.InetSocketAddress;

/**
 * A {@link ChannelInboundHandlerAdapter} implementation that handles upstream messages from Netty.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
@Sharable
public final class ArravChannelHandler extends ChannelInboundHandlerAdapter {
	
	/**
	 * A default access level constructor to discourage external instantiation outside of the {@code net.edge.net} package.
	 */
	ArravChannelHandler() { }
	
	/**
	 * A concurrent {@link Multiset} containing active connections.
	 */
	private final Multiset<String> connections = ConcurrentHashMultiset.create();
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		String address = Session.address(ctx);
		if(UpdateCommand.inProgess == 2) {
			Session.write(ctx, LoginCode.SERVER_BEING_UPDATED);
			return;
		}
		if(Application.STARTING) {
			Session.write(ctx, LoginCode.SERVER_STARTING);
			return;
		}
		if(HostManager.contains(address, HostListType.BANNED_IP)) {
			Session.write(ctx, LoginCode.ACCOUNT_DISABLED);
			return;
		}
		int limit = NetworkConstants.CONNECTION_AMOUNT;
		if(connections.count(address) >= limit) { // Reject if more than CONNECTION_LIMIT active connections.
			//Session.write(ctx, LoginCode.LOGIN_LIMIT_EXCEEDED);
			//return;
		}
		ChannelFuture future = ctx.channel().closeFuture(); // Remove address once disconnected.
		future.addListener(it -> connections.remove(address));
		connections.add(address);
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		Session session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
		if (session == null) {
			throw new IllegalStateException("session == null");
		}
		session.unregister();
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.fireChannelActive();
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Session session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
		if(session != null) {
			session.terminate();
		}
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			Session session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
			if (session == null) {
				throw new IllegalStateException("session == null");
			}
			session.handleMessage(ctx, msg);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.fireChannelReadComplete();
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if(evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if(event.state() == IdleState.READER_IDLE) {
				ctx.channel().close();
			}
		}
	}
	
	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		ctx.fireChannelWritabilityChanged();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
		if(NetworkConstants.IGNORED_NETWORK_EXCEPTIONS.stream().noneMatch($it -> Objects.equal($it, e.getMessage()))) {
			e.printStackTrace();
		}
		ctx.close();
	}
	
}