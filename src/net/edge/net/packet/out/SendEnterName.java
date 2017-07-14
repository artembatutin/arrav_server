package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.PacketType;
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
	public boolean onSent(Player player) {
		player.setEnterInputListener(Optional.of(action));
		return true;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(187, PacketType.VARIABLE_BYTE);
		msg.putCString(title);
		msg.endVarSize();
		return msg.getBuffer();
	}
}
