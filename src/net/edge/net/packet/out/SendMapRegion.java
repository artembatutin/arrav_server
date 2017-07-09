package net.edge.net.packet.out;

import net.edge.locale.Position;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendMapRegion implements OutgoingPacket {
	private final Position  position;
	
	public SendMapRegion(Position position) {
		this.position = position;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(73);
		msg.putShort(position.getRegionX() + 6, ByteTransform.A);
		msg.putShort(position.getRegionY() + 6);
	}
}
