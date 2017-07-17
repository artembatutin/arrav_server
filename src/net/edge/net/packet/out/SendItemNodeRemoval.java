package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.item.GroundItem;

public final class SendItemNodeRemoval implements OutgoingPacket {
	
	private final GroundItem item;
	
	public SendItemNodeRemoval(GroundItem item) {
		this.item = item;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		new SendCoordinates(item.getPosition()).write(player, msg);
		msg.message(156);
		msg.put(0, ByteTransform.S);
		msg.putShort(item.getItem().getId());
		return msg.getBuffer();
	}
}
