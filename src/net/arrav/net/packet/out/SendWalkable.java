package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendWalkable implements OutgoingPacket {
	
	private final int id;
	
	public SendWalkable(int id) {
		this.id = id;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(208);
		buf.putInt(id);
		return buf;
	}
}
