package net.arrav.net.packet.in;

import io.netty.buffer.ByteBuf;
import net.arrav.net.packet.IncomingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class DefaultPacket implements IncomingPacket {

	@Override
	public void handle(Player player, int opcode, int size, ByteBuf buf) {

	}
}
