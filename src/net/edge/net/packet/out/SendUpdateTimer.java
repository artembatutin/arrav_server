package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteOrder;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendUpdateTimer implements OutgoingPacket {
	
	private final int timer;
	
	public SendUpdateTimer(int timer) {
		this.timer = timer;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(114);
		buf.putShort(timer, ByteOrder.LITTLE);
		return buf;
	}
}
