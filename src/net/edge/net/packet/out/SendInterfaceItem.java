package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.GameBuffer;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendInterfaceItem implements OutgoingPacket {
	
	private final int widget, itemId;
	
	public SendInterfaceItem(int widget, int itemId) {
		this.widget = widget;
		this.itemId = itemId;
	}
	
	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(82);
		msg.putInt(widget);
		msg.putInt(itemId);
		return msg.getBuffer();
	}
}
