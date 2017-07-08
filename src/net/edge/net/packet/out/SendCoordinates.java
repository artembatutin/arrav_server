package net.edge.net.packet.out;

import net.edge.locale.Position;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendCoordinates implements OutgoingPacket {
	
	private final Position position;
	
	public SendCoordinates(Position position) {
		this.position = position;
	}
	
	@Override
	public void write(Player player) {
		if(player.getLastRegion() == null)
			player.setLastRegion(player.getPosition().copy());
		GameBuffer msg = player.getSession().getStream();
		msg.message(85);
		msg.put(position.getY() - (player.getLastRegion().getRegionY() * 8), ByteTransform.C);
		msg.put(position.getX() - (player.getLastRegion().getRegionX() * 8), ByteTransform.C);
	}
}
