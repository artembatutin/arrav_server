package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.game.GamePacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.container.ItemContainer;

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
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(53, GamePacketType.VARIABLE_SHORT);
		buf.putShort(id);
		if(container.getItems() == null) {
			buf.putShort(0);
			buf.putShort(0);
			buf.put(0);
			buf.putShort(0, ByteTransform.A, ByteOrder.LITTLE);
		} else {
			int count = container.size();
			buf.putShort(container.capacity());
			buf.putShort(count);
			for(Item item : container.getItems()) {
				if(count == 0)
					break;
				if(item != null) {
					count--;
					if(item.getAmount() > 254) {
						buf.put(255);
						buf.putInt(item.getAmount(), ByteOrder.INVERSE_MIDDLE);
					} else {
						buf.put(item.getAmount());
					}
					boolean noted = (id >= 270 && id <= 279) || (id == 3900);
					buf.putShort(item.getId() + (noted ? 0 : 1), ByteTransform.A, ByteOrder.LITTLE);
					if(id == 3900) {
						if(item.getValue().getPrice() > 254) {
							buf.put(255);
							buf.putInt(item.getValue().getPrice(), ByteOrder.INVERSE_MIDDLE);
						} else {
							buf.put(item.getValue().getPrice());
						}
					}
				} else {
					buf.put(0);
					buf.putShort(0, ByteTransform.A, ByteOrder.LITTLE);
					if(id == 3900) {
						buf.put(0);
					}
				}
			}
		}
		buf.endVarSize();
		return buf;
	}
}
