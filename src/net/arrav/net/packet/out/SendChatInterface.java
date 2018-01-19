package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

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
