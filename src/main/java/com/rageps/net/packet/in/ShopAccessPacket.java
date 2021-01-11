//package com.rageps.net.packet.in;
//
//import com.rageps.content.market.MarketCounter;
//import com.rageps.net.codec.game.GamePacket;
//import com.rageps.net.packet.IncomingPacket;
//import com.rageps.world.entity.actor.player.Player;
//import com.rageps.world.locale.Position;
//
///**
// * The message sent from the client which depends on the Mob Information panel integration.
// * @author Artem Batutin
// */
//public final class ShopAccessPacket implements IncomingPacket {
//
//	@Override
//	public void handle(Player player, int opcode, int size, GamePacket buf) {
//		if(opcode == 20) {//shop opening
//			int shop = buf.getShort();
//			if(player.getPosition().withinDistance(new Position(3078, 3510), 3)) {
//				if(MarketCounter.getShops().containsKey(shop)) {
//					MarketCounter.getShops().get(shop).openShop(player);
//				}
//			}
//		}
//	}
//}
