package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.game.GamePacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.util.ActionListener;
import net.edge.world.entity.actor.player.Player;

import java.util.Optional;
import java.util.function.Function;

public final class SendEnterAmount implements OutgoingPacket {
	
	private final String title;
	private final Function<String, ActionListener> action;
	
	public SendEnterAmount(String title, Function<String, ActionListener> action) {
		this.title = title;
		this.action = action;
	}
	
	@Override
	public boolean onSent(Player player) {
		player.setEnterInputListener(Optional.of(action));
		return true;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(27, GamePacketType.VARIABLE_BYTE);
		buf.putCString(title);
		buf.endVarSize();
		return buf;
	}
}
