package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.item.container.ItemContainer;

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
					boolean noted = (id >= 270 && id <= 279) || (id == 3900);
					out.putShort(item.getId() + (noted ? 0 : 1), ByteTransform.A, ByteOrder.LITTLE);
					if(id == 3900) {
						if(item.getValue().getPrice() > 254) {
							out.put(255);
							out.putInt(item.getValue().getPrice(), ByteOrder.INVERSE_MIDDLE);
						} else {
							out.put(item.getValue().getPrice());
						}
					}
				} else {
					out.put(0);
					out.putShort(0, ByteTransform.A, ByteOrder.LITTLE);
					if(id == 3900) {
						out.put(0);
					}
				}
			}
		}
		out.endVarSize();
		return out;
	}
}
