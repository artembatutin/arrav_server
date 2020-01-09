package net.arrav.net.packet.out;

import io.netty.buffer.ByteBuf;
import net.arrav.content.market.MarketItem;
import net.arrav.net.codec.ByteOrder;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.game.GamePacket;
import net.arrav.net.codec.game.GamePacketType;
import net.arrav.net.packet.OutgoingPacket;
import net.arrav.world.entity.actor.player.Player;

public final class SendShopStock implements OutgoingPacket {
	
	private final MarketItem item;
	
	public SendShopStock(MarketItem item) {
		this.item = item;
	}
	
	@Override
	public GamePacket write(Player player, ByteBuf buf) {
		GamePacket out = new GamePacket(this, buf);
		out.message(55, GamePacketType.VARIABLE_SHORT);
		if(item.getStock() > 254) {
			out.put(255);
			out.putInt(item.getStock(), ByteOrder.INVERSE_MIDDLE);
		} else {
			out.put(item.getStock());
		}
		out.putShort(item.getId() + 1, ByteTransform.A, ByteOrder.LITTLE);
		out.endVarSize();
		return out;
	}
}
