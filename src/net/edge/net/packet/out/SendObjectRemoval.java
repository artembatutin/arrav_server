package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteTransform;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.object.GameObject;

public final class SendObjectRemoval implements OutgoingPacket {
	
	private final GameObject object;
	
	public SendObjectRemoval(GameObject object) {
		this.object = object;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		new SendCoordinates(object.getGlobalPos()).write(player, buf);
		buf.message(101);
		buf.put((object.getObjectType().getId() << 2) + (object.getDirection().getId() & 3), ByteTransform.C);
		buf.put(0);
		return buf;
	}
}
