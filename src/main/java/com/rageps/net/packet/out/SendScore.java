package com.rageps.net.packet.out;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import io.netty.buffer.ByteBuf;

public final class SendScore implements OutgoingPacket {
	
	private final String title;
	private final int index, kills, deaths, killstreak;
	
	public SendScore(int index, String title, int kills, int deaths, int killstreak) {
		this.index = index;
		this.title = title;
		this.kills = kills;
		this.deaths = deaths;
		this.killstreak = killstreak;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(30, GamePacketType.VARIABLE_BYTE);
		out.putShort(index);
		out.putShort(kills);
		out.putShort(deaths);
		out.putShort(killstreak);
		out.putCString(title);
		out.endVarSize();
		return out;
	}
}
