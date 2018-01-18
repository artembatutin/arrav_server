package net.edge.net.packet.in;

import io.netty.buffer.ByteBuf;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.entity.actor.player.Player;

public final class DefaultPacket implements IncomingPacket {

	@Override
	public void handle(Player player, int opcode, int size, ByteBuf buf) {

	}
}
