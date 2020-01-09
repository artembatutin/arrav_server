package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.game.GamePacket;
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
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(246);
		out.putShort(id, ByteOrder.LITTLE);
		out.putShort(zoom);
		out.putShort(model);
		return out;
	}
}
