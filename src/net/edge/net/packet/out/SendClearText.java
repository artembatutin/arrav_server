package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.PacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendClearText implements OutgoingPacket {

	private final int start, count;

	public SendClearText(int start, int count) {
		this.start = start;
		this.count = count;
	}

	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		for(int i = start; i < start + count; i++) {
			player.interfaceTexts.remove(i);
		}
		msg.message(127, PacketType.FIXED);
		msg.putShort(start, ByteTransform.A);
		msg.putShort(count, ByteTransform.A);
		return msg.getBuffer();
	}
}
