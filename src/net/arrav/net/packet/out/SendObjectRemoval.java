package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.object.GameObject;

public final class SendObjectRemoval implements OutgoingPacket {
	
	private final GameObject object;
	
	public SendObjectRemoval(GameObject object) {
		this.object = object;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		new SendCoordinates(object.getPosition()).write(player, buf);
		buf.message(101);
		buf.put((object.getObjectType().getId() << 2) + (object.getDirection().getId() & 3), ByteTransform.C);
		buf.put(0);
		return buf;
	}
}
