package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

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
