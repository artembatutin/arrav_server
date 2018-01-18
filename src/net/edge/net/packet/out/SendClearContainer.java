package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendClearContainer implements OutgoingPacket {
	
	private final int id;
	
	public SendClearContainer(int id) {
		this.id = id;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(40);
		buf.putShort(id);
		return buf;
	}
}
