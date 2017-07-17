package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.actor.player.Player;

public final class SendUpdateTimer implements OutgoingPacket {
	
	private final int timer;
	
	public SendUpdateTimer(int timer) {
		this.timer = timer;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(114);
		msg.putShort(timer, ByteOrder.LITTLE);
		return msg.getBuffer();
	}
}
