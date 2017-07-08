package net.edge.net.packet.out;

import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.MessageType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendMessage implements OutgoingPacket {
	
	private final String message;
	
	public SendMessage(String message) {
		this.message = message;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(253, MessageType.VARIABLE);
		msg.putCString(message);
		msg.endVarSize();
	}
}
