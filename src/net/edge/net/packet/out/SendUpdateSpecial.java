package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteOrder;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendUpdateSpecial implements OutgoingPacket {
	
	private final int id, amount;
	
	public SendUpdateSpecial(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(70);
		buf.putShort(amount);
		buf.putShort(0, ByteOrder.LITTLE);
		buf.putShort(id, ByteOrder.LITTLE);
		return buf;
	}
}
