package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

public final class SendYell implements OutgoingPacket {
	
	private final String author, message;
	private final Rights rank;
	
	public SendYell(String author, String message, Rights rank) {
		this.author = author;
		this.message = message;
		this.rank = rank;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(210, GamePacketType.VARIABLE_BYTE);
		buf.putCString(author);
		buf.putCString(message);
		buf.putShort(rank.getProtocolValue());
		buf.endVarSize();
		return buf;
	}
}
