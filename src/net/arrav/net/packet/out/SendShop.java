package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.arrav.content.market.MarketItem;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendShop implements OutgoingPacket {
	
	private final int id;
	private final IntArrayList items;
	
	public SendShop(int id, IntArrayList items) {
		this.id = id;
		this.items = items;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(53, GamePacketType.VARIABLE_SHORT);
		buf.putShort(id);
		if(items == null) {
			buf.putShort(0);
			buf.putShort(0);
			buf.put(0);
			buf.putShort(0, ByteTransform.A, ByteOrder.LITTLE);
		} else {
			buf.putShort(items.size());
			buf.putShort(items.size());
			for(int i : items) {
				MarketItem item = MarketItem.get(i);
				if(item != null) {
					buf.put(item.isUnlimitedStock() ? 1 : 0);
					if(!item.isUnlimitedStock()) {
						if(item.getStock() > 254) {
							buf.put(255);
							buf.putInt(item.getStock(), ByteOrder.INVERSE_MIDDLE);
						} else {
							buf.put(item.getStock());
						}
					}
					boolean noted = (id >= 270 && id <= 279) || (id == 3900);
					buf.putShort(item.getId() + (noted ? 0 : 1), ByteTransform.A, ByteOrder.LITTLE);
					if(item.getPrice() > 254) {
						buf.put(255);
						buf.putInt(item.getPrice(), ByteOrder.INVERSE_MIDDLE);
					} else {
						buf.put(item.getPrice());
					}
				} else {
					buf.put(0);
					buf.putShort(0, ByteTransform.A, ByteOrder.LITTLE);
					buf.put(0);
					buf.put(0);
				}
			}
		}
		buf.endVarSize();
		return buf;
	}
}
