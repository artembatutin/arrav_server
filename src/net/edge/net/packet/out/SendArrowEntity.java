package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.Actor;
import net.edge.world.entity.actor.player.Player;

public final class SendArrowEntity implements OutgoingPacket {
	
	private final Actor entity;
	
	public SendArrowEntity(Actor entity) {
		this.entity = entity;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(248);
		msg.put(entity.isMob() ? 1 : 10);
		msg.putShort(entity.getSlot());
		msg.put(0);
		return msg.getBuffer();
	}
	
}
