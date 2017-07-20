package net.edge.net.codec.login;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.edge.world.entity.actor.player.assets.Rights;

/**
 * A {@link MessageToByteEncoder} implementation that encodes and writes the data contained within the {@link
 * LoginResponse} to a buffer that will be sent to the client.
 * @author lare96 <http://github.org/lare96>
 */
@Sharable
public final class LoginEncoder extends MessageToByteEncoder<LoginResponse> {
	
	@Override
	protected void encode(ChannelHandlerContext ctx, LoginResponse msg, ByteBuf out) throws Exception {
		out.writeByte(msg.getResponse().getCode());
		if(msg.getResponse() == LoginCode.NORMAL) {
			out.writeByte(msg.getRights() == Rights.PLAYER && msg.isIron() ? Rights.IRON_MAN.getProtocolValue() : msg.getRights().getProtocolValue());
		}
	}
}