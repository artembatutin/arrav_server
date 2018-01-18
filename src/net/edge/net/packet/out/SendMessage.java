package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.game.GamePacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendMessage implements OutgoingPacket {
	
	private final String message;
	
	public SendMessage(String message) {
		this.message = message;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(253, GamePacketType.VARIABLE_BYTE);
		buf.putCString(message);
		buf.endVarSize();
		return buf;
	}
}
