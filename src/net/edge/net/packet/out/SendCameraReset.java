package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendCameraReset implements OutgoingPacket {
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(107);
		return msg.getBuffer();
	}
}
