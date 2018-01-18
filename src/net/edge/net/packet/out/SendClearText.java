package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.game.GamePacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendClearText implements OutgoingPacket {
	
	private final int start, count;
	
	public SendClearText(int start, int count) {
		this.start = start;
		this.count = count;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		for(int i = start; i < start + count; i++) {
			player.interfaceTexts.remove(i);
		}
		buf.message(127, GamePacketType.FIXED);
		buf.putShort(start, ByteTransform.A);
		buf.putShort(count, ByteTransform.A);
		return buf;
	}
}
