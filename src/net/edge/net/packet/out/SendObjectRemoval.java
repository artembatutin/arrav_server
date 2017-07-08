package net.edge.net.packet.out;

import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectNode;

public final class SendObjectRemoval implements OutgoingPacket {
	
	private final ObjectNode object;
	
	public SendObjectRemoval(ObjectNode object) {
		this.object = object;
	}
	
	@Override
	public void write(Player player) {
		player.write(new SendCoordinates(object.getGlobalPos()));
		GameBuffer msg = player.getSession().getStream();
		msg.message(101);
		msg.put((object.getObjectType().getId() << 2) + (object.getDirection().getId() & 3), ByteTransform.C);
		msg.put(0);
	}
}
