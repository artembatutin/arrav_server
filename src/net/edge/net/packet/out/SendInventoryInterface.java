package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteTransform;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendInventoryInterface implements OutgoingPacket {
	
	private final int open, overlay;
	
	public SendInventoryInterface(int open, int overlay) {
		this.open = open;
		this.overlay = overlay;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(248);
		buf.putShort(open, ByteTransform.A);
		buf.putShort(overlay);
		return buf;
	}
}
