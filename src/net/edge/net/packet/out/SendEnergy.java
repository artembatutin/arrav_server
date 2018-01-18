package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendEnergy implements OutgoingPacket {
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(110);
		buf.put((int) player.getRunEnergy());
		return buf;
	}
}
