package com.rageps.net.packet.out;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import io.netty.buffer.ByteBuf;

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
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(217, GamePacketType.VARIABLE_BYTE);
		out.putCString(author);
		out.putCString(message);
		out.putCString(clanName);
		out.putShort(rank.getProtocolValue());
		out.endVarSize();
		return out;
	}
}
