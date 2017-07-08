package net.edge.net.packet.out;

import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.MessageType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

public final class SendYell implements OutgoingPacket {
	
	private final String author, message;
	private final Rights rank;
	
	public SendYell(String author, String message, Rights rank) {
		this.author = author;
		this.message = message;
		this.rank = rank;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(210, MessageType.VARIABLE);
		msg.putCString(author);
		msg.putCString(message);
		msg.putShort(rank.getProtocolValue());
		msg.endVarSize();
	}
}
