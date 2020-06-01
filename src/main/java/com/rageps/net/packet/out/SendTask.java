package com.rageps.net.packet.out;

import io.netty.buffer.ByteBuf;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;

public final class SendTask implements OutgoingPacket {
	
	private final String task;
	
	public SendTask(String task) {
		this.task = task;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(90, GamePacketType.VARIABLE_SHORT);
		out.putCString(task);
		out.endVarSize();
		return out;
	}
}
