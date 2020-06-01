package com.rageps.net.packet.out;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.Actor;
import com.rageps.world.entity.actor.player.Player;
import io.netty.buffer.ByteBuf;

public final class SendArrowEntity implements OutgoingPacket {
	
	private final Actor entity;
	
	public SendArrowEntity(Actor entity) {
		this.entity = entity;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(248);
		out.put(entity.isMob() ? 1 : 10);
		out.putShort(entity.getSlot());
		out.put(0);
		return out;
	}
	
}
