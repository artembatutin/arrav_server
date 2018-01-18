package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.game.GamePacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

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
