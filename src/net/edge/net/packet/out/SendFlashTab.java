package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteTransform;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendFlashTab implements OutgoingPacket {
	
	private final int code;
	
	public SendFlashTab(int code) {
		this.code = code;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(24);
		buf.put(code, ByteTransform.A);
		return buf;
	}
}
