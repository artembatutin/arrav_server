package net.edge.fs;

import java.util.logging.Logger;

import net.edge.fs.cache.CacheLoader;
import net.edge.fs.net.ChannelHandler;
import net.edge.fs.net.JagGrabPipelineFactory;
import net.edge.fs.net.OnDemandPipelineFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;

/**
 * The main class of the FileServer.
 * Services are started here..
 * 
 * @author Professor Oak
 * 
 * Credits to:
 * @author Graham
 * @author Nikki
 * For references from their update-server in the 
 * Apollo source.
 */
public final class FileServer {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(FileServer.class.getName());

	/**
	 * The cache loader.
	 * Loads and stores the cache.
	 */
	private static CacheLoader cache;
	
	/**
	 * Starts the FileServer system.
	 * @throws Exception 	if an error occurs.
	 */
	public static void init() throws Exception {
		
		logger.info("Starting FileServer..");
		
		//Attempt to load cache
		logger.info("Loading cache..");
		cache = new CacheLoader();
		cache.init();
		logger.info("Cache loaded.");
		
		//Attempt to bind services..
		logger.info("Binding services..");
		final ChannelHandler handler = new ChannelHandler();
		bind("JagGrab", FileServerConstants.JAGGRAB_PORT, new JagGrabPipelineFactory(handler));
		bind("OnDemand", FileServerConstants.ONDEMAND_PORT, new OnDemandPipelineFactory(handler));
		logger.info("Services bound.");
		
		logger.info("FileServer started.");
	}
	
	/**
	 * Attempts to bind the FileServer service to said port.
	 * @param service		The service to bind.	
	 * @param port			The port to bind the service to.
	 * @param initializer	The pipeline factory for the service.
	 */
	private static void bind(String service, int port, ChannelInitializer<SocketChannel> initializer) {
		logger.info("Binding "+service+" service to port "+port+"..");
		
		ResourceLeakDetector.setLevel(Level.DISABLED);
		EventLoopGroup loopGroup = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(loopGroup).channel(NioServerSocketChannel.class)
		.childHandler(initializer).bind(port).syncUninterruptibly();
		
		logger.info(""+service+" service has been bound to port "+port+".");
	}
	
	/**
	 * Gets the loaded cache. 
	 * @return		The cache.
	 */
	public static CacheLoader getCache() {
		return cache;
	}
	
	/** START **/
	public static void main(String[] args) {
		try {
			FileServer.init();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
