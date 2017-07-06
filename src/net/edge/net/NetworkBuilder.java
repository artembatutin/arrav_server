package net.edge.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;
import net.edge.Server;

import java.io.IOException;

/**
 * The net builder for the Runescape #317 protocol. This class is used to
 * start and configure the {@link ServerBootstrap} that will control and manage
 * the entire net.
 * @author lare96 <http://github.com/lare96>
 */
public final class NetworkBuilder {
	
	/**
	 * The bootstrap that will oversee the management of the entire net.
	 */
	private final ServerBootstrap bootstrap = new ServerBootstrap();
	
	/**
	 * The event loop group that will be attached to the bootstrap.
	 */
	private final EventLoopGroup loopGroup = new NioEventLoopGroup();
	
	/**
	 * The {@link ChannelInitializer} that will determine how channels will be
	 * initialized when registered to the event loop group.
	 */
	private final ChannelInitializer<SocketChannel> channelInitializer = new EdgevilleChannelInitializer();
	
	/**
	 * Initializes this net handler effectively preparing the server to
	 * listen for connections and handle net events.
	 * @param port the port that this net will be bound to.
	 * @throws IOException if any issues occur while starting the net.
	 */
	public void initialize(int port) throws IOException {
		ResourceLeakDetector.setLevel(Level.SIMPLE);
		bootstrap.group(loopGroup);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.childHandler(channelInitializer);
		bootstrap.bind(port).syncUninterruptibly();
	}
	
}
