package net.arrav.net.packet.out;


import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

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
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
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
