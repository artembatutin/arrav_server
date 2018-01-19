package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendInterfacePlayerModel implements OutgoingPacket {
	
	private final int id;
	
	public SendInterfacePlayerModel(int id) {
		this.id = id;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(185);
		buf.putShort(id, ByteTransform.A, ByteOrder.LITTLE);
		return buf;
	}
}
