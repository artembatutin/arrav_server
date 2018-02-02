package net.arrav.net.packet.in;

import io.netty.buffer.ByteBuf;
import net.arrav.content.market.MarketCounter;
import net.arrav.net.packet.IncomingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.locale.Position;

/**
 * The message sent from the client which depends on the Mob Information panel integration.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class ShopAccessPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, ByteBuf buf) {
		if(opcode == 20) {//shop opening
			int shop = buf.getShort();
			if(player.getPosition().withinDistance(new Position(3078, 3510), 3)) {
				if(MarketCounter.getShops().containsKey(shop)) {
					MarketCounter.getShops().get(shop).openShop(player);
				}
			}
		}
	}
}
