package net.edge.net;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ipfilter.AbstractRemoteAddressFilter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import net.edge.Application;
import net.edge.content.commands.impl.UpdateCommand;
import net.edge.net.codec.login.LoginCode;
import net.edge.net.codec.login.LoginResponse;
import net.edge.net.host.HostListType;
import net.edge.net.host.HostManager;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * An {@link AbstractRemoteAddressFilter} implementation that filters {@link Channel}s by the amount of active connections
 * they already have and whether or not they are blacklisted. A threshold is put on the amount of successful connections
 * allowed to be made in order to provide security from socket flooder attacks.
 * <strong>One {@code EdgevilleChannelFilter} instance must be shared across all pipelines in order to ensure that every channel
 * is using the same multiset.</strong>
 *
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author lare96 <http://github.org/lare96>
 */
@Sharable
public final class EdgevilleChannelFilter extends AbstractRemoteAddressFilter<InetSocketAddress> {

	/**
	 * An {@link AttributeKey} used to access an {@link Attribute} describing which {@link LoginCode} should be sent for
	 * rejected channels.
	 */
	private static final AttributeKey<LoginCode> RESPONSE_KEY = AttributeKey.valueOf("channel.RESPONSE_KEY");

	/**
	 * A concurrent {@link Multiset} containing active connections.
	 */
	private final Multiset<String> connections = ConcurrentHashMultiset.create();

	@Override
	protected boolean accept(ChannelHandlerContext ctx, InetSocketAddress remoteAddress) throws Exception {
		String address = address(remoteAddress);
		if(UpdateCommand.inProgess == 2) {
			response(ctx, LoginCode.SERVER_BEING_UPDATED);
			return false;
		}
		if(Application.STARTING) {
			response(ctx, LoginCode.SERVER_STARTING);
			return false;
		}
		if(HostManager.contains(address, HostListType.BANNED_IP)) {
			response(ctx, LoginCode.ACCOUNT_DISABLED);
			return false;
		}
		int limit = NetworkConstants.CONNECTION_AMOUNT;
		if(connections.count(address) >= limit) { // Reject if more than CONNECTION_LIMIT active connections.
			response(ctx, LoginCode.LOGIN_LIMIT_EXCEEDED);
			return false;
		}
		return true;
	}

	@Override
	protected void channelAccepted(ChannelHandlerContext ctx, InetSocketAddress remoteAddress) {
		String address = address(remoteAddress);
		ChannelFuture future = ctx.channel().closeFuture(); // Remove address once disconnected.
		future.addListener(it -> connections.remove(address));
		System.out.println(address);
		connections.add(address);
	}

	@Override
	protected ChannelFuture channelRejected(ChannelHandlerContext ctx, InetSocketAddress remoteAddress) {
		Channel channel = ctx.channel();
		LoginCode response = channel.attr(RESPONSE_KEY).get(); // Retrieve the response message.
		LoginResponse message = new LoginResponse(response);
		ByteBuf initialMessage = ctx.alloc().buffer(8).writeLong(0); // Write initial message.
		channel.write(initialMessage, channel.voidPromise());
		return channel.writeAndFlush(message).addListener(ChannelFutureListener.CLOSE); // Write response message.
	}

	/**
	 * Retrieves the host address name from the {@link InetSocketAddress}.
	 */
	private String address(InetSocketAddress remoteAddress) {
		InetAddress inet = remoteAddress.getAddress();
		return inet.getHostAddress();
	}

	/**
	 * Sets the {@code RESPONSE_KEY} attribute to {@code response}.
	 */
	private void response(ChannelHandlerContext ctx, LoginCode response) {
		Channel channel = ctx.channel();
		channel.attr(RESPONSE_KEY).set(response);
	}
}