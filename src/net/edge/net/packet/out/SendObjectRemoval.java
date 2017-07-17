package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.object.ObjectNode;

public final class SendObjectRemoval implements OutgoingPacket {
	
	private final ObjectNode object;
	
	public SendObjectRemoval(ObjectNode object) {
		this.object = object;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		new SendCoordinates(object.getGlobalPos()).write(player, msg);
		msg.message(101);
		msg.put((object.getObjectType().getId() << 2) + (object.getDirection().getId() & 3), ByteTransform.C);
		msg.put(0);
		return msg.getBuffer();
	}
}
