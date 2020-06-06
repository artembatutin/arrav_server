package com.rageps.net.packet.out;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.player.Player;
import io.netty.buffer.ByteBuf;

public final class SendFeedMessage implements OutgoingPacket {

	private final String message;
	private final String color;

	public SendFeedMessage(String message, String color) {
		this.message = message;
		this.color = color;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(175, GamePacketType.VARIABLE_BYTE);
		out.putCString(message);
		out.putCString(color);
		return out;
	}

}
