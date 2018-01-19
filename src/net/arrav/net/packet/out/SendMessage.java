package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

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
