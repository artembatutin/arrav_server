package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendInterface implements OutgoingPacket {
	
	private final int id;
	
	public SendInterface(int id) {
		this.id = id;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(97);
		buf.putShort(id);
		return buf;
	}
}
