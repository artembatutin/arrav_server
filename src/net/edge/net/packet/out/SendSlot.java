package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendSlot implements OutgoingPacket {
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(249);
		msg.put(1, ByteTransform.A);
		msg.putShort(player.getSlot(), ByteTransform.A, ByteOrder.LITTLE);
		return msg.getBuffer();
	}
}
