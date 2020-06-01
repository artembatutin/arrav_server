package com.rageps.net.packet.out;

import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import io.netty.buffer.ByteBuf;

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
