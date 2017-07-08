package net.edge.net.packet.out;

import net.edge.locale.Position;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendClearContainer implements OutgoingPacket {
	
	private final int id;
	
	public SendClearContainer(int id) {
		this.id = id;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(40);
		msg.putShort(id);
	}
}
