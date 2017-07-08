package net.edge.net.packet.out;

import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.ItemNode;

public final class SendItemNodeRemoval implements OutgoingPacket {
	
	private final ItemNode item;
	
	public SendItemNodeRemoval(ItemNode item) {
		this.item = item;
	}
	
	@Override
	public void write(Player player) {
		player.write(new SendCoordinates(item.getPosition()));
		GameBuffer msg = player.getSession().getStream();
		msg.message(156);
		msg.put(0, ByteTransform.S);
		msg.putShort(item.getItem().getId());
	}
}
