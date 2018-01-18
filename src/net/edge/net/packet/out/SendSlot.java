package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendSlot implements OutgoingPacket {
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(249);
		buf.put(1, ByteTransform.A);
		buf.putShort(player.getSlot(), ByteTransform.A, ByteOrder.LITTLE);
		return buf;
	}
}
