package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
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
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(246);
		msg.putShort(id, ByteOrder.LITTLE);
		msg.putShort(zoom);
		msg.putShort(model);
		return msg.getBuffer();
	}
}
