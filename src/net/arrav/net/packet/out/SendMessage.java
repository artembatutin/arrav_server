package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendMessage implements OutgoingPacket {
	
	private final String message;
	
	public SendMessage(String message) {
		this.message = message;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(253, GamePacketType.VARIABLE_BYTE);
		out.putCString(message);
		out.endVarSize();
		return out;
	}
}
