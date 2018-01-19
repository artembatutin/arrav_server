package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendInterfaceItem implements OutgoingPacket {
	
	private final int widget, itemId;
	
	public SendInterfaceItem(int widget, int itemId) {
		this.widget = widget;
		this.itemId = itemId;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(82);
		buf.putInt(widget);
		buf.putInt(itemId);
		return buf;
	}
}
