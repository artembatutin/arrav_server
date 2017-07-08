package net.edge.net.packet.out;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.edge.content.market.MarketItem;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.GameBuffer;
import net.edge.net.codec.MessageType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.node.entity.player.Player;

public final class SendShopPrice implements OutgoingPacket {
	
	private final MarketItem item;
	
	public SendShopPrice(MarketItem item) {
		this.item = item;
	}
	
	@Override
	public void write(Player player) {
		GameBuffer msg = player.getSession().getStream();
		msg.message(54, MessageType.VARIABLE_SHORT);
		if(item.getPrice() > 254) {
			msg.put(255);
			msg.putInt(item.getPrice(), ByteOrder.INVERSE_MIDDLE);
		} else {
			msg.put(item.getPrice());
		}
		msg.putShort(item.getId() + 1, ByteTransform.A, ByteOrder.LITTLE);
		msg.endVarSize();
	}
}
