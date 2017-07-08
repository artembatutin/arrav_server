package net.edge.net.packet.out;

import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.MessageType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.util.ActionListener;
import net.edge.world.node.entity.player.Player;

import java.util.Optional;
import java.util.function.Function;

public final class SendLink implements OutgoingPacket {
	
	private final String link;
	
	public SendLink(String link) {
		this.link = link;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(100, MessageType.VARIABLE);
		msg.putCString(link);
		msg.endVarSize();
	}
}
