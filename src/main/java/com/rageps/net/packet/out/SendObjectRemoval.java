package com.rageps.net.packet.out;

import io.netty.buffer.ByteBuf;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;

public final class SendObjectRemoval implements OutgoingPacket {
	
	private final GameObject object;
	
	public SendObjectRemoval(GameObject object) {
		this.object = object;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(101);
		out.put((object.getObjectType().getId() << 2) + (object.getDirection().getId() & 3), ByteTransform.C);
		out.put(0);
		return out;
	}
	
	@Override
	public OutgoingPacket coordinatePacket() {
		return new SendCoordinates(object.getPosition());
	}
}
