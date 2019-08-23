package net.arrav.net.packet.out;


import net.arrav.net.codec.game.GamePacket;
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
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
		out.message(217, GamePacketType.VARIABLE_BYTE);
		out.putCString(author);
		out.putCString(message);
		out.putCString(clanName);
		out.putShort(rank.getProtocolValue());
		out.endVarSize();
		return out;
	}
}
