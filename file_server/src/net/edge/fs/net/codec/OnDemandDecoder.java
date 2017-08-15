package net.edge.fs.net.codec;

import java.util.List;

import net.edge.fs.request.impl.OnDemandRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * A {@link ByteToMessageDecoder} for decoding incoming OnDemand requests.
 * @author Professor Oak
 */
public final class OnDemandDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {

		//Make sure we have proper amount of bytes in the buffer
		//For the data we'll be reading..
		if (buf.readableBytes() >= 5) {
			//Read request attributes..
			//Read file type
			int fileType = buf.readUnsignedByte() + 1;
			//Read file id
			int fileId = buf.readInt();
			//Create the request..
			out.add(new OnDemandRequest(ctx.channel(), fileType, fileId));
		}
	}

}
