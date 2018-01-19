package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.GroundItem;

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
