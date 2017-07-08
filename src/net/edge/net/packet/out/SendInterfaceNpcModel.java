package net.edge.net.packet.out;

import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendInterfaceNpcModel implements OutgoingPacket {
	
	private final int id, model;
	
	public SendInterfaceNpcModel(int id, int model) {
		this.id = id;
		this.model = model;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(75);
		msg.putShort(model, ByteTransform.A, ByteOrder.LITTLE);
		msg.putShort(id, ByteTransform.A, ByteOrder.LITTLE);
	}
}
