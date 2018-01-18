package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendMinimapState implements OutgoingPacket {
	
	private final int code;
	
	public SendMinimapState(int code) {
		this.code = code;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(99);
		buf.put(code);
		return buf;
	}
}
