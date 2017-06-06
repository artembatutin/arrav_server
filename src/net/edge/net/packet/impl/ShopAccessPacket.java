package net.edge.net.packet.impl;

import net.edge.content.market.MarketCounter;
import net.edge.locale.Position;
import net.edge.net.codec.ByteMessage;
import net.edge.net.packet.PacketReader;
import net.edge.world.node.entity.player.Player;

/**
 * The message sent from the client which depends on the Npc Information panel integration.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class ShopAccessPacket implements PacketReader {
	
	@Override
	public void handle(Player player, int opcode, int size, ByteMessage payload) {
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
