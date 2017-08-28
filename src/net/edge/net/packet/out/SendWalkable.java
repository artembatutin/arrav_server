package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendWalkable implements OutgoingPacket {

	private final int id;

	public SendWalkable(int id) {
		this.id = id;
	}

	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(208);
		msg.putInt(id);
		return msg.getBuffer();
	}
}
