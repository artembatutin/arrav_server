package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.game.GamePacket;
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
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(80);
		out.put(start);
		out.put(duration);
		out.put(end);
		return out;
	}
}
