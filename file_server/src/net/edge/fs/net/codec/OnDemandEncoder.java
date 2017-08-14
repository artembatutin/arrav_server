package net.edge.fs.net.codec;

import net.edge.fs.response.impl.OnDemandResponse;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * A {@link MessageToByteEncoder} for encoding outgoing OnDemand responses.
 * @author Professor Oak
 */
public class OnDemandEncoder extends MessageToByteEncoder<OnDemandResponse> {

	@Override
	protected void encode(ChannelHandlerContext ctx, OnDemandResponse response, ByteBuf out) throws Exception {
		
		final int fileType = response.getFileType();
		final int fileId = response.getFileId();
		final int fileSize = response.getFileSize();
		final int chunkId = response.getChunkId();
		final ByteBuf file = response.getBuffer();

		out.writeByte(fileType - 1);
		out.writeMedium(fileId);
		out.writeInt(fileSize);
		out.writeShort(chunkId);
		out.writeBytes(file);
	}
}
