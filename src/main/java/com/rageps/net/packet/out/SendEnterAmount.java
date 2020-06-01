package com.rageps.net.packet.out;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.util.ActionListener;
import com.rageps.world.entity.actor.player.Player;
import io.netty.buffer.ByteBuf;

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
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(27, GamePacketType.VARIABLE_BYTE);
		out.putCString(title);
		out.endVarSize();
		return out;
	}
}
