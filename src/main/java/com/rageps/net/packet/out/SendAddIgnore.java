package com.rageps.net.packet.out;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import io.netty.buffer.ByteBuf;

public final class SendAddIgnore implements OutgoingPacket {

	private final long username;

	public SendAddIgnore(long username) {
		this.username = username;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(214);
		out.putLong(username);
		return out;
	}
}
