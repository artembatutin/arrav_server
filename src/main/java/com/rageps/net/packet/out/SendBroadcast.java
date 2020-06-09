package com.rageps.net.packet.out;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import io.netty.buffer.ByteBuf;

public final class SendBroadcast implements OutgoingPacket {
	
	private final int id;
	private final int time;
	private final Object context;

	public SendBroadcast(int id, int time, Object context) {
		this.id = id;
		this.time = time;
		this.context = context;
	}

	public SendBroadcast(int id, Object context) {
		this.id = id;
		this.time = -1;
		this.context = context;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(114, GamePacketType.VARIABLE_BYTE);
		out.putShort(id);
		out.putShort(time * 3000);
		out.putCString(String.valueOf(context));
		return out;
	}
}
