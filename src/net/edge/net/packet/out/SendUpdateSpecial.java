package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendUpdateSpecial implements OutgoingPacket {
	
	private final int id, amount;
	
	public SendUpdateSpecial(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(70);
		msg.putShort(amount);
		msg.putShort(0, ByteOrder.LITTLE);
		msg.putShort(id, ByteOrder.LITTLE);
		return msg.getBuffer();
	}
}
