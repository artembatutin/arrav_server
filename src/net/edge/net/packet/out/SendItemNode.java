package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.GroundItem;

public final class SendItemNode implements OutgoingPacket {
	
	private final GroundItem item;
	
	public SendItemNode(GroundItem item) {
		this.item = item;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		new SendCoordinates(item.getPosition()).write(player, buf);
		buf.message(44);
		buf.putShort(item.getItem().getId(), ByteTransform.A, ByteOrder.LITTLE);
		buf.putShort(item.getItem().getAmount());
		buf.put(0);
		return buf;
	}
}
