//package com.rageps.net.packet.in;
//
//import com.rageps.net.codec.game.GamePacket;
//import com.rageps.net.packet.IncomingPacket;
//import com.rageps.world.entity.actor.player.Player;
//
//public final class PrivacyOptionPacket implements IncomingPacket {
//
//	@Override
//	public void handle(Player player, int opcode, int size, GamePacket buf) {
//		final int publicMode = buf.get();
//		final int privateMode = buf.get();
//		final int tradeMode = buf.get();
//		final int clanMode = buf.get();
//		player.relations.setPrivacyChatModes(publicMode, privateMode, clanMode, tradeMode);
//	}
//}
