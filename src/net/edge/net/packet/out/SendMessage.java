package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.PacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendMessage implements OutgoingPacket {

	private final String message;

	public SendMessage(String message) {
		this.message = message;
	}

	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(253, PacketType.VARIABLE_BYTE);
		msg.putCString(message);
		msg.endVarSize();
		return msg.getBuffer();
	}
}
