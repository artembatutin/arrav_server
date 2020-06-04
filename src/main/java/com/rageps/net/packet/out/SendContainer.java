package com.rageps.net.packet.out;

import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.container.ItemContainer;
import io.netty.buffer.ByteBuf;

public final class SendContainer implements OutgoingPacket {
	
	private final int id;
	private final ItemContainer container;
	
	public SendContainer(int id, ItemContainer container) {
		this.id = id;
		this.container = container;
	}
	
	@Override
	public boolean onSent(Player player) {
		if(id == -1)
			return false;
		if(container.size() == 0) {
			player.out(new SendClearContainer(id));
			return false;
		}
		return true;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(53, GamePacketType.VARIABLE_SHORT);
		out.putShort(id);
		if(container.getItems() == null) {
			out.putShort(0);
			out.putShort(0);
			out.put(0);
			out.putShort(0, ByteTransform.A, ByteOrder.LITTLE);
		} else {
			int count = container.size();
			out.putShort(container.capacity());
			out.putShort(count);
			for(Item item : container.getItems()) {
				if(count == 0)
					break;
				if(item != null) {
					count--;
					if(item.getAmount() > 254) {
						out.put(255);
						out.putInt(item.getAmount(), ByteOrder.INVERSE_MIDDLE);
					} else {
						out.put(item.getAmount());
					}
					out.putShort(item.getId() + 1, ByteTransform.A, ByteOrder.LITTLE);
				} else {
					out.put(0);
					out.putShort(0, ByteTransform.A, ByteOrder.LITTLE);
				}
			}
		}
		out.endVarSize();
		return out;
	}
}