package net.edge.net.packet.out;

import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;
import net.edge.world.object.ObjectNode;

public final class SendRemoveObjects implements OutgoingPacket {
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(131);
	}
}
