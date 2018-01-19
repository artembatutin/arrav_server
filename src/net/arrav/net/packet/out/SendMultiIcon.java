package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendMultiIcon implements OutgoingPacket {
	
	private final boolean hide;
	
	public SendMultiIcon(boolean hide) {
		this.hide = hide;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(61);
		buf.put(hide ? 0 : 1);
		return buf;
	}
}
