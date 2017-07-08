package net.edge.net.packet.out;

import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendChatInterface implements OutgoingPacket {
	
	private final int id;
	
	public SendChatInterface(int id) {
		this.id = id;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(164);
		msg.putShort(id, ByteOrder.LITTLE);
	}
}
