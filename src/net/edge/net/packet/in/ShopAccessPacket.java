package net.edge.net.packet.in;

import net.edge.content.market.MarketCounter;
import net.edge.world.locale.Position;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.entity.actor.player.Player;

/**
 * The message sent from the client which depends on the Mob Information panel integration.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class ShopAccessPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
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
