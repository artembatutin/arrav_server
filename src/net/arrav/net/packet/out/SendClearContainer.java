package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

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
