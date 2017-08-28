package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendCloseInterface implements OutgoingPacket {
	
	@Override
	public boolean onSent(Player player) {
		player.getDialogueBuilder().interrupt();
		return true;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(219);
		return msg.getBuffer();
	}
	
}
