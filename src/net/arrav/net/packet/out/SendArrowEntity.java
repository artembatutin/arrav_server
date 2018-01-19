package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.Actor;
import net.arrav.world.entity.actor.player.Player;

public final class SendArrowEntity implements OutgoingPacket {
	
	private final Actor entity;
	
	public SendArrowEntity(Actor entity) {
		this.entity = entity;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(248);
		buf.put(entity.isMob() ? 1 : 10);
		buf.putShort(entity.getSlot());
		buf.put(0);
		return buf;
	}
	
}
