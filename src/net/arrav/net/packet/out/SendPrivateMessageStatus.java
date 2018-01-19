package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendPrivateMessageStatus implements OutgoingPacket {
	
	private final int code;
	
	public SendPrivateMessageStatus(int code) {
		this.code = code;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(221);
		buf.put(code);
		return buf;
	}
}
