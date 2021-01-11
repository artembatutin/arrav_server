//package com.rageps.net.packet.in;
//
//import com.rageps.content.dialogue.impl.StatementDialogue;
//import com.rageps.content.market.MarketShop;
//import com.rageps.net.codec.game.GamePacket;
//import com.rageps.net.packet.IncomingPacket;
//import com.rageps.util.TextUtils;
//import com.rageps.world.entity.actor.player.Player;
//
///**
// * The message that is sent from the client when the player searches an item in the market.
// * @author Artem Batutin
// */
//public final class MarketPacket implements IncomingPacket {
//
//	@Override
//	public void handle(Player player, int opcode, int size, GamePacket buf) {
//		String search = TextUtils.hashToName(buf.getLong());
//		if(player.isIronMan() && !player.isIronMaxed()) {
//			player.getDialogueBuilder().append(new StatementDialogue("You are in iron man mode.", "Therefore you can't search the global market.", "Once you max-out you will be able to."));
//			return;
//		}
//		new MarketShop(player, search);
//	}
//}
