package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.item.ItemNode;

public final class SendItemNode implements OutgoingPacket {
	
	private final ItemNode item;
	
	public SendItemNode(ItemNode item) {
		this.item = item;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		new SendCoordinates(item.getPosition()).write(player, msg);
		msg.message(44);
		msg.putShort(item.getItem().getId(), ByteTransform.A, ByteOrder.LITTLE);
		msg.putShort(item.getItem().getAmount());
		msg.put(0);
		return msg.getBuffer();
	}
}
