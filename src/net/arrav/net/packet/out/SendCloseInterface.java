package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendCloseInterface implements OutgoingPacket {
	
	@Override
	public boolean onSent(Player player) {
		player.getDialogueBuilder().interrupt();
		return true;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(219);
		return buf;
	}
	
}
