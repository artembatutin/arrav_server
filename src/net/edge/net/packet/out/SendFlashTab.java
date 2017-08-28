package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendFlashTab implements OutgoingPacket {

	private final int code;

	public SendFlashTab(int code) {
		this.code = code;
	}

	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(24);
		msg.put(code, ByteTransform.A);
		return msg.getBuffer();
	}
}
