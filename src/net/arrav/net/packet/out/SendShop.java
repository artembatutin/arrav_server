package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.arrav.content.market.MarketItem;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
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
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(53, GamePacketType.VARIABLE_SHORT);
		out.putShort(id);
		if(items == null) {
			out.putShort(0);
			out.putShort(0);
			out.put(0);
			out.putShort(0, ByteTransform.A, ByteOrder.LITTLE);
		} else {
			out.putShort(items.size());
			out.putShort(items.size());
			for(int i : items) {
				MarketItem item = MarketItem.get(i);
				if(item != null) {
					out.put(item.isUnlimitedStock() ? 1 : 0);
					if(!item.isUnlimitedStock()) {
						if(item.getStock() > 254) {
							out.put(255);
							out.putInt(item.getStock(), ByteOrder.INVERSE_MIDDLE);
						} else {
							out.put(item.getStock());
						}
					}
					boolean noted = (id >= 270 && id <= 279) || (id == 3900);
					out.putShort(item.getId() + (noted ? 0 : 1), ByteTransform.A, ByteOrder.LITTLE);
					if(item.getPrice() > 254) {
						out.put(255);
						out.putInt(item.getPrice(), ByteOrder.INVERSE_MIDDLE);
					} else {
						out.put(item.getPrice());
					}
				} else {
					out.put(0);
					out.putShort(0, ByteTransform.A, ByteOrder.LITTLE);
					out.put(0);
					out.put(0);
				}
			}
		}
		out.endVarSize();
		return out;
	}
}
