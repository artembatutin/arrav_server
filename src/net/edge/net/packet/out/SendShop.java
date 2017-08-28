package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.edge.content.market.MarketItem;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.PacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendShop implements OutgoingPacket {

	private final int id;
	private final IntArrayList items;

	public SendShop(int id, IntArrayList items) {
		this.id = id;
		this.items = items;
	}

	@Override
	public ByteBuf write(Player player, GameBuffer msg) {
		msg.message(53, PacketType.VARIABLE_SHORT);
		msg.putShort(id);
		if(items == null) {
			msg.putShort(0);
			msg.putShort(0);
			msg.put(0);
			msg.putShort(0, ByteTransform.A, ByteOrder.LITTLE);
		} else {
			msg.putShort(items.size());
			msg.putShort(items.size());
			for(int i : items) {
				MarketItem item = MarketItem.get(i);
				if(item != null) {
					msg.put(item.isUnlimitedStock() ? 1 : 0);
					if(!item.isUnlimitedStock()) {
						if(item.getStock() > 254) {
							msg.put(255);
							msg.putInt(item.getStock(), ByteOrder.INVERSE_MIDDLE);
						} else {
							msg.put(item.getStock());
						}
					}
					boolean noted = (id >= 270 && id <= 279) || (id == 3900);
					msg.putShort(item.getId() + (noted ? 0 : 1), ByteTransform.A, ByteOrder.LITTLE);
					if(item.getPrice() > 254) {
						msg.put(255);
						msg.putInt(item.getPrice(), ByteOrder.INVERSE_MIDDLE);
					} else {
						msg.put(item.getPrice());
					}
				} else {
					msg.put(0);
					msg.putShort(0, ByteTransform.A, ByteOrder.LITTLE);
					msg.put(0);
					msg.put(0);
				}
			}
		}
		msg.endVarSize();
		return msg.getBuffer();
	}
}
