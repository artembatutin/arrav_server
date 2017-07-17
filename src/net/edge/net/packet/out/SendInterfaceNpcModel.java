package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.actor.player.Player;

public final class SendInterfaceNpcModel implements OutgoingPacket {
	
	private final int id, model;
	
	public SendInterfaceNpcModel(int id, int model) {
		this.id = id;
		this.model = model;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(75);
		msg.putShort(model, ByteTransform.A, ByteOrder.LITTLE);
		msg.putShort(id, ByteTransform.A, ByteOrder.LITTLE);
		return msg.getBuffer();
	}
}
