package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendClearContainer implements OutgoingPacket {

	private final int id;

	public SendClearContainer(int id) {
		this.id = id;
	}

	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(40);
		msg.putShort(id);
		return msg.getBuffer();
	}
}
