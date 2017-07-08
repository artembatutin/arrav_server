package net.edge.net.packet.out;

import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendMapRegion implements OutgoingPacket {
	
	@Override
	public void write(Player player) {
		player.setLastRegion(player.getPosition().copy());
		player.setUpdates(true, false);
		player.setUpdateRegion(true);
		GameBuffer msg = player.getSession().getStream();
		msg.message(73);
		msg.putShort(player.getPosition().getRegionX() + 6, ByteTransform.A);
		msg.putShort(player.getPosition().getRegionY() + 6);
	}
}
