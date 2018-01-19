package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendRemoveObjects implements OutgoingPacket {
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(131);
		return buf;
	}
}
