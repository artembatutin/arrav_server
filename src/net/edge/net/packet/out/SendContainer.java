package net.edge.net.packet.out;

import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.PacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.container.ItemContainer;

public final class SendContainer implements OutgoingPacket {
	
	private final int id;
	private final ItemContainer container;
	
	public SendContainer(int id, ItemContainer container) {
		this.id = id;
		this.container = container;
	}
	
	@Override
	public void write(Player player) {
		if(id == -1)
			return;
		if(container.size() == 0) {
			player.write(new SendClearContainer(id));
			return;
		}
		GameBuffer msg = player.getSession().getStream();
		msg.message(53, PacketType.VARIABLE_SHORT);
		msg.putShort(id);
		if(container.getItems() == null) {
			msg.putShort(0);
			msg.putShort(0);
			msg.put(0);
			msg.putShort(0, ByteTransform.A, ByteOrder.LITTLE);
		} else {
			int count = container.size();
			msg.putShort(container.capacity());
			msg.putShort(count);
			for(Item item : container.getItems()) {
				if(count == 0)
					break;
				if(item != null) {
					count--;
					if(item.getAmount() > 254) {
						msg.put(255);
						msg.putInt(item.getAmount(), ByteOrder.INVERSE_MIDDLE);
					} else {
						msg.put(item.getAmount());
					}
					boolean noted = (id >= 270 && id <= 279) || (id == 3900);
					msg.putShort(item.getId() + (noted ? 0 : 1), ByteTransform.A, ByteOrder.LITTLE);
					if(id == 3900) {
						if(item.getValue().getPrice() > 254) {
							msg.put(255);
							msg.putInt(item.getValue().getPrice(), ByteOrder.INVERSE_MIDDLE);
						} else {
							msg.put(item.getValue().getPrice());
						}
					}
				} else {
					msg.put(0);
					msg.putShort(0, ByteTransform.A, ByteOrder.LITTLE);
					if(id == 3900) {
						msg.put(0);
					}
				}
			}
		}
		msg.endVarSize();
	}
}
