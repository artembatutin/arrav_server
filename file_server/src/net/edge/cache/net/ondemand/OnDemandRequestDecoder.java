package net.edge.cache.net.ondemand;


import net.edge.cache.fs.FileDescriptor;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * A {@link FrameDecoder} for the 'on-demand' protocol.
 * @author Graham
 */
public final class OnDemandRequestDecoder extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel c, ChannelBuffer buf) throws Exception {
		if(buf.readableBytes() >= 6) {
			int type = buf.readUnsignedByte() + 1;
			int file = buf.readInt();
			int priority = buf.readUnsignedByte();
			
			FileDescriptor desc = new FileDescriptor(type, file);
			OnDemandRequest.Priority p = OnDemandRequest.Priority.valueOf(priority);
			
			return new OnDemandRequest(desc, p);
		}
		return null;
	}

}
