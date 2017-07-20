package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendMultiIcon implements OutgoingPacket {
	
	private final boolean hide;
	
	public SendMultiIcon(boolean hide) {
		this.hide = hide;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(61);
		msg.put(hide ? 0 : 1);
		return msg.getBuffer();
	}
}
