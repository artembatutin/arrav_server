package net.edge.net.packet.out;

import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendWalkable implements OutgoingPacket {
	
	private final int id;
	
	public SendWalkable(int id) {
		this.id = id;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(208);
		msg.putInt(id);
	}
}
