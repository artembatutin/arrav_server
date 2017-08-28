package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.PacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendTask implements OutgoingPacket {

	private final String task;

	public SendTask(String task) {
		this.task = task;
	}

	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(90, PacketType.VARIABLE_SHORT);
		msg.putCString(task);
		msg.endVarSize();
		return msg.getBuffer();
	}
}
