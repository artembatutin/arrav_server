package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendFade implements OutgoingPacket {
	
	private final int start, duration, end;
	
	public SendFade(int start, int duration, int end) {
		this.start = start;
		this.duration = duration;
		this.end = end;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(80);
		buf.put(start);
		buf.put(duration);
		buf.put(end);
		return buf;
	}
}
