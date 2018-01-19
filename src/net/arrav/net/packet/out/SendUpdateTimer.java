package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

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
