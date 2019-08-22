package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendSlot implements OutgoingPacket {
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(249);
		buf.put(1, ByteTransform.A);
		buf.putShort(player.getSlot(), ByteTransform.A, ByteOrder.LITTLE);
		
		System.out.println("slot: " + player.getSlot());
		return buf;
	}
}
