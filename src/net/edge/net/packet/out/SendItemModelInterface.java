package net.edge.net.packet.out;

import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendItemModelInterface implements OutgoingPacket {
	
	private final int id, zoom, model;
	
	public SendItemModelInterface(int id, int zoom, int model) {
		this.id = id;
		this.zoom = zoom;
		this.model = model;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(246);
		msg.putShort(id, ByteOrder.LITTLE);
		msg.putShort(zoom);
		msg.putShort(model);
	}
}
