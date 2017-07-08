package net.edge.net.packet.out;

import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.MessageType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendText implements OutgoingPacket {
	
	private final int id;
	private final String text;
	
	public SendText(int id, String text) {
		this.id = id;
		this.text = text;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(126, MessageType.VARIABLE_SHORT);
		msg.putCString(text);
		msg.putShort(id, ByteTransform.A);
		msg.endVarSize();
		msg.endVarSize();
	}
}
