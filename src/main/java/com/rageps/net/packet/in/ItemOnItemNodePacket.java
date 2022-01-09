//package com.rageps.net.packet.in;
//
//import com.rageps.net.codec.ByteOrder;
//import com.rageps.net.codec.ByteTransform;
//import com.rageps.net.codec.game.GamePacket;
//import com.rageps.net.packet.IncomingPacket;
//import com.rageps.world.entity.actor.player.Player;
//
///**
// * The message sent from the client when a player uses an item on another item.
// * @author lare96 <http://github.com/lare96>
// */
//public final class ItemOnItemNodePacket implements IncomingPacket {
//
//	@Override
//	public void handle(Player player, int opcode, int size, GamePacket buf) {
//		final int itemX = buf.getShort(true, ByteOrder.LITTLE);
//		final int itemY = buf.getShort(true, ByteTransform.A, ByteOrder.LITTLE);
//		final int itemId = buf.getShort(ByteTransform.A);
//	}
//}
