package net.edge.fs.net;

import net.edge.fs.FileServerConstants;
import net.edge.fs.net.codec.JagGrabDecoder;
import net.edge.fs.net.codec.JagGrabEncoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * A {@link ChannelInitializer} for a JagGrab channel.
 * @author Graham
 * @author Professor Oak
 */
public final class JagGrabPipelineFactory extends ChannelInitializer<SocketChannel> {

	/**
	 * The channel event handler.
	 */
	private final ChannelHandler handler;
	
	/**
	 * Creates a JagGrab pipeline factory.
	 * @param handler The channel event handler.
	 */
	public JagGrabPipelineFactory(ChannelHandler handler) {
		this.handler = handler;
	}
	
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		final ChannelPipeline pipeline = channel.pipeline();
		// decoders
		pipeline.addLast("string-decoder", new StringDecoder(FileServerConstants.JAGGRAB_CHARSET));
		pipeline.addLast("decoder", new JagGrabDecoder());
		// encoders
		pipeline.addLast("encoder", new JagGrabEncoder());
		// handler
		pipeline.addLast("timeout", new IdleStateHandler(FileServerConstants.SESSION_TIMEOUT, 0, 0));
		pipeline.addLast("handler", handler);
	}
}
