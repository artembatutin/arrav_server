package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendMoveComponent implements OutgoingPacket {
	
	private final int id, x, y;
	
	public SendMoveComponent(int id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(70);
		buf.putShort(x);
		buf.putShort(y, ByteOrder.LITTLE);
		buf.putShort(id, ByteOrder.LITTLE);
		return buf;
	}
}
