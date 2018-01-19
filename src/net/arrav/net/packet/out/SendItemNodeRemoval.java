package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.GroundItem;

public final class SendItemNodeRemoval implements OutgoingPacket {
	
	private final GroundItem item;
	
	public SendItemNodeRemoval(GroundItem item) {
		this.item = item;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		new SendCoordinates(item.getPosition()).write(player, buf);
		buf.message(156);
		buf.put(0, ByteTransform.S);
		buf.putShort(item.getItem().getId());
		return buf;
	}
}
