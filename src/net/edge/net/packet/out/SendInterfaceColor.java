package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendInterfaceColor implements OutgoingPacket {
	
	private final int id, color;
	
	public SendInterfaceColor(int id, int color) {
		this.id = id;
		this.color = color;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(122);
		buf.putShort(id, ByteTransform.A, ByteOrder.LITTLE);
		buf.putShort(color, ByteTransform.A, ByteOrder.LITTLE);
		return buf;
	}
}
