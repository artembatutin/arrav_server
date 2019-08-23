package net.arrav.net.packet.out;

import net.arrav.content.market.MarketItem;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendShopPrice implements OutgoingPacket {
	
	private final MarketItem item;
	
	public SendShopPrice(MarketItem item) {
		this.item = item;
	}
	
	@Override
	public GamePacket write(Player player) {
		GamePacket out = new GamePacket(this);
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
