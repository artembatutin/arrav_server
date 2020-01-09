package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.game.GamePacket;
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
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(210, GamePacketType.VARIABLE_BYTE);
		out.putCString(author);
		out.putCString(message);
		out.putShort(rank.getProtocolValue());
		out.endVarSize();
		return out;
	}
}
