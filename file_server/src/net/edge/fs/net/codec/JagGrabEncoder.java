package net.edge.fs.net.codec;

import net.edge.fs.response.impl.JagGrabResponse;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * A {@link MessageToByteEncoder} for encoding outgoing JagGrab responses.
 * @author Professor Oak
 */
public class JagGrabEncoder extends MessageToByteEncoder<JagGrabResponse> {

	@Override
	protected void encode(ChannelHandlerContext ctx, JagGrabResponse response, ByteBuf out) throws Exception {
		
		final ByteBuf file = response.getBuffer();
		final int file_size = file.readableBytes();
		out.writeInt(file_size);
		out.writeBytes(file);
	}
}
