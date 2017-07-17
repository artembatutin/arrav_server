package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendRemoveObjects implements OutgoingPacket {
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(131);
		return msg.getBuffer();
	}
}
