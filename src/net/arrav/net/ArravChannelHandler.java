package net.arrav.net;

import com.google.common.base.Objects;
import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import net.arrav.Arrav;
import net.arrav.content.commands.impl.UpdateCommand;
import net.arrav.net.codec.login.LoginCode;
import net.arrav.net.host.HostListType;
import net.arrav.net.host.HostManager;

/**
 * A {@link ChannelInboundHandlerAdapter} implementation that handles upstream messages from Netty.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
@Sharable
public final class ArravChannelHandler extends ChannelInboundHandlerAdapter {
	
	/**
	 * A default access level constructor to discourage external instantiation outside of the {@code net.arrav.net} package.
	 */
	ArravChannelHandler() {
	}
	
	/**
	 * A concurrent {@link Multiset} containing active connections.
	 */
	private final Multiset<String> connections = ConcurrentHashMultiset.create();
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) {
		String address = Session.address(ctx);
		if(UpdateCommand.inProgess == 2) {
			Session.write(ctx, LoginCode.SERVER_BEING_UPDATED);
			return;
		}
		if(Arrav.STARTING) {
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
	public void channelUnregistered(ChannelHandlerContext ctx) {
		Session session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
		if(session == null) {
			throw new IllegalStateException("session == null");
		}
		session.unregister();
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		ctx.fireChannelActive();
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		Session session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
		if(session != null) {
			session.terminate();
		}
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		try {
			Session session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
			if(session == null) {
				throw new IllegalStateException("session == null");
			}
			session.handleMessage(ctx, msg);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.fireChannelReadComplete();
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
		if(evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if(event.state() == IdleState.READER_IDLE) {
				ctx.channel().close();
			}
		}
	}
	
	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) {
		ctx.fireChannelWritabilityChanged();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
		if(NetworkConstants.IGNORED_NETWORK_EXCEPTIONS.stream().noneMatch($it -> Objects.equal($it, e.getMessage()))) {
			e.printStackTrace();
		}
		ctx.close();
	}
	
}