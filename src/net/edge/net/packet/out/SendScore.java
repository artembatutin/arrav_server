package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.game.GamePacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

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
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(30, GamePacketType.VARIABLE_BYTE);
		buf.putShort(index);
		buf.putShort(kills);
		buf.putShort(deaths);
		buf.putShort(killstreak);
		buf.putCString(title);
		buf.endVarSize();
		return buf;
	}
}
