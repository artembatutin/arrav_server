package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendInterfaceNpcModel implements OutgoingPacket {
	
	private final int id, model;
	
	public SendInterfaceNpcModel(int id, int model) {
		this.id = id;
		this.model = model;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(75);
		buf.putShort(model, ByteTransform.A, ByteOrder.LITTLE);
		buf.putShort(id, ByteTransform.A, ByteOrder.LITTLE);
		return buf;
	}
}
