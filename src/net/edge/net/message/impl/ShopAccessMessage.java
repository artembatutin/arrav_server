package net.edge.net.message.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.message.InputMessageListener;
import net.edge.world.content.market.MarketCounter;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.locale.Position;

/**
 * The message sent from the client which depends on the Npc Information panel integration.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class ShopAccessMessage implements InputMessageListener {
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
		if(opcode == 20) {//shop opening
			int shop = payload.getShort();
			if(player.getPosition().withinDistance(new Position(3080, 3507), 2)) {
				if(MarketCounter.getShops().containsKey(shop)) {
					MarketCounter.getShops().get(shop).openShop(player);
				}
			}
		}
	}
}
