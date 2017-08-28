package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendInterfacePlayerModel implements OutgoingPacket {

	private final int id;

	public SendInterfacePlayerModel(int id) {
		this.id = id;
	}

	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(185);
		msg.putShort(id, ByteTransform.A, ByteOrder.LITTLE);
		return msg.getBuffer();
	}
}
