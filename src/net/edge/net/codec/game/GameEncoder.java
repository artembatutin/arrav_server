package net.edge.net.codec.game;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToByteEncoder;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

/**
 * A {@link MessageToByteEncoder} which encodes in-game packets.
 * @author Graham
 */
public final class GameEncoder extends MessageToByteEncoder<OutgoingPacket> {
	
	private final Player player;
	
	public GameEncoder(Player player) {
		this.player = player;
	}
	
	@Override
	protected void encode(io.netty.channel.ChannelHandlerContext ctx, OutgoingPacket msg, ByteBuf out) throws Exception {
		msg.write(player);
	}
}