package com.rageps.net.packet.out;

import io.netty.buffer.ByteBuf;
import com.rageps.content.market.MarketItem;
import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.codec.game.GamePacketType;
import com.rageps.net.packet.OutgoingPacket;
import com.rageps.world.entity.actor.player.Player;

public final class SendShopPrice implements OutgoingPacket {
	
	private final MarketItem item;
	
	public SendShopPrice(MarketItem item) {
		this.item = item;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(54, GamePacketType.VARIABLE_SHORT);
		if(item.getPrice() > 254) {
			out.put(255);
			out.putInt(item.getPrice(), ByteOrder.INVERSE_MIDDLE);
		} else {
			out.put(item.getPrice());
		}
		out.putShort(item.getId() + 1, ByteTransform.A, ByteOrder.LITTLE);
		out.endVarSize();
		return out;
	}
}
