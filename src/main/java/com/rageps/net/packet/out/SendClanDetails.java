package com.rageps.net.packet.out;

import com.rageps.content.clanchannel.ClanRank;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import io.netty.buffer.ByteBuf;

public final class SendClanDetails implements OutgoingPacket {
	
	private final String author, message, clanName;
	private final ClanRank rank;
	
	public SendClanDetails(String author, String message, String clanName, ClanRank rank) {
		this.author = author;
		this.message = message;
		this.clanName = clanName;
		this.rank = rank;
	}
	public SendClanDetails(String message, String clan, ClanRank rank) {
		this("", message, clan, rank);
	}

	public SendClanDetails(String message, String clan) {
		this("", message, clan, ClanRank.MEMBER);
	}


	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(217, GamePacketType.VARIABLE_BYTE);
		out.putCString(author);
		out.putCString(message);
		out.putCString(clanName);
		out.putShort(rank.rank);
		out.endVarSize();
		return out;
	}
}
