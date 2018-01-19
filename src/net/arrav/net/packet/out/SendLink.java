package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendLink implements OutgoingPacket {
	
	private final String link;
	
	public SendLink(String link) {
		this.link = link;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(100, GamePacketType.VARIABLE_BYTE);
		buf.putCString(link);
		buf.endVarSize();
		return buf;
	}
}
