package com.rageps.net.packet.out;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import io.netty.buffer.ByteBuf;

public final class SendTooltip implements OutgoingPacket {

	private final String string;
	private final int id;

	public SendTooltip(String string, int id) {
	this.string = string;
	this.id = id;
	}

	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(203, GamePacketType.VARIABLE_BYTE);
		out.putInt(id);
		out.putCString(string);
		return out;
	}
}
