//package com.rageps.net.packet.in;
//
//import com.rageps.net.codec.game.GamePacket;
//import com.rageps.net.packet.IncomingPacket;
//import com.rageps.world.entity.actor.player.Player;
//import com.rageps.world.entity.actor.player.assets.activity.ActivityManager.ActivityType;
//
///**
// * The message sent from the client when the player clicks on the 'Click this to
// * continue' link to forward a dialogue.
// * @author lare96 <http://github.com/lare96>
// */
//public final class AdvanceDialoguePacket implements IncomingPacket {
//
//	@Override
//	public void handle(Player player, int opcode, int size, GamePacket buf) {
//		if(player.getActivityManager().contains(ActivityType.DIALOGUE_INTERACTION))
//			return;
//		player.getDialogueBuilder().advance();
//		player.getActivityManager().execute(ActivityType.DIALOGUE_INTERACTION);
//	}
//}
