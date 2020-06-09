package com.rageps.net.packet.out;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import io.netty.buffer.ByteBuf;

public final class SendPrivateMessageListStatus implements OutgoingPacket {
	
	private final int code;
	
	public SendPrivateMessageListStatus(int code) {
		this.code = code;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(221);
		out.put(code);
		return out;
	}
}
