package net.edge.net.packet.out;

import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.PacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

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
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(217, PacketType.VARIABLE_BYTE);
		msg.putCString(author);
		msg.putCString(message);
		msg.putCString(clanName);
		msg.putShort(rank.getProtocolValue());
		msg.endVarSize();
	}
}
