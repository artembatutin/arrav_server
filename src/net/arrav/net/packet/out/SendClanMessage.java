package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

public final class SendClanMessage implements OutgoingPacket {
	
	private final String author, message, clanName;
	private final Rights rank;
	
	public SendClanMessage(String author, String message, String clanName, Rights rank) {
		this.author = author;
		this.message = message;
		this.clanName = clanName;
		this.rank = rank;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(217, GamePacketType.VARIABLE_BYTE);
		buf.putCString(author);
		buf.putCString(message);
		buf.putCString(clanName);
		buf.putShort(rank.getProtocolValue());
		buf.endVarSize();
		return buf;
	}
}
