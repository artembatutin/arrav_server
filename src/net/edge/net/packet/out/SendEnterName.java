package net.edge.net.packet.out;

import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.MessageType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.util.ActionListener;
import net.edge.world.node.entity.player.Player;

import java.util.Optional;
import java.util.function.Function;

public final class SendEnterName implements OutgoingPacket {
	
	private final String title;
	private final Function<String, ActionListener> action;
	
	public SendEnterName(String title, Function<String, ActionListener> action) {
		this.title = title;
		this.action = action;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(187, MessageType.VARIABLE);
		msg.putCString(title);
		msg.endVarSize();
		player.setEnterInputListener(Optional.of(action));
	}
}
