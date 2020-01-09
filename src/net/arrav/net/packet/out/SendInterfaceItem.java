package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendInterfaceItem implements OutgoingPacket {
	
	private final int widget, itemId;
	
	public SendInterfaceItem(int widget, int itemId) {
		this.widget = widget;
		this.itemId = itemId;
	}
	
	@Override
	public GamePacket write(Player playe, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(82);
		out.putInt(widget);
		out.putInt(itemId);
		return out;
	}
}
