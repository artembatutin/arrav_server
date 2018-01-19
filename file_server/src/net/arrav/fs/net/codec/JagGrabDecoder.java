package net.arrav.fs.net.codec;


import java.util.List;

import net.arrav.fs.request.impl.JagGrabRequest;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * A {@link MessageToMessageDecoder} for decoding incoming JagGrab requests.
 * @author Professor Oak
 */
public final class JagGrabDecoder extends MessageToMessageDecoder<String> {

	@Override
	protected void decode(ChannelHandlerContext ctx, String path, List<Object> out) throws Exception {
		
		//Make sure the JagGrab file path is valid..
		if(path.length() == 0) {
			return;
		}
		
		//Create the new JagGrab request!
		out.add(new JagGrabRequest(ctx.channel(), path));
	}
}
