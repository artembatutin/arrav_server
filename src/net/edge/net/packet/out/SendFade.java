package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.actor.player.Player;

public final class SendFade implements OutgoingPacket {
	
	private final int start, duration, end;
	
	public SendFade(int start, int duration, int end) {
		this.start = start;
		this.duration = duration;
		this.end = end;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(80);
		msg.put(start);
		msg.put(duration);
		msg.put(end);
		return msg.getBuffer();
	}
}
