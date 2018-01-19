package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendTask implements OutgoingPacket {
	
	private final String task;
	
	public SendTask(String task) {
		this.task = task;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(90, GamePacketType.VARIABLE_SHORT);
		buf.putCString(task);
		buf.endVarSize();
		return buf;
	}
}
