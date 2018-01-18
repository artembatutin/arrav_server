package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteOrder;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendChatInterface implements OutgoingPacket {
	
	private final int id;
	
	public SendChatInterface(int id) {
		this.id = id;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(164);
		buf.putShort(id, ByteOrder.LITTLE);
		return buf;
	}
}
