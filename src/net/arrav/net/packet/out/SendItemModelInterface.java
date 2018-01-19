package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendItemModelInterface implements OutgoingPacket {
	
	private final int id, zoom, model;
	
	public SendItemModelInterface(int id, int zoom, int model) {
		this.id = id;
		this.zoom = zoom;
		this.model = model;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(246);
		buf.putShort(id, ByteOrder.LITTLE);
		buf.putShort(zoom);
		buf.putShort(model);
		return buf;
	}
}
