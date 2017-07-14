package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.PacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

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
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(30, PacketType.VARIABLE_BYTE);
		msg.putShort(index);
		msg.putShort(kills);
		msg.putShort(deaths);
		msg.putShort(killstreak);
		msg.putCString(title);
		msg.endVarSize();
		return msg.getBuffer();
	}
}
