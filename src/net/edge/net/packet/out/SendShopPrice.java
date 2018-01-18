package net.edge.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.edge.content.market.MarketItem;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.codec.game.GamePacketType;
import net.edge.net.packet.OutgoingPacket;
import net.edge.world.entity.actor.player.Player;

public final class SendShopPrice implements OutgoingPacket {
	
	private final MarketItem item;
	
	public SendShopPrice(MarketItem item) {
		this.item = item;
	}
	
	@Override
	public ByteBuf write(Player player, ByteBuf buf) {
		buf.message(54, GamePacketType.VARIABLE_SHORT);
		if(item.getPrice() > 254) {
			buf.put(255);
			buf.putInt(item.getPrice(), ByteOrder.INVERSE_MIDDLE);
		} else {
			buf.put(item.getPrice());
		}
		buf.putShort(item.getId() + 1, ByteTransform.A, ByteOrder.LITTLE);
		buf.endVarSize();
		return buf;
	}
}
