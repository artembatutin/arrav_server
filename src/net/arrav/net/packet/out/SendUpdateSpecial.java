package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

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
