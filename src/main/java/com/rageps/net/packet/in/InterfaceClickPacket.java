//package com.rageps.net.packet.in;
//
//import com.rageps.content.minigame.MinigameHandler;
//import com.rageps.net.codec.game.GamePacket;
//import com.rageps.net.packet.IncomingPacket;
//import com.rageps.world.entity.actor.player.Player;
//import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
//import com.rageps.world.entity.item.container.session.ExchangeSessionManager;
//
///**
// * The message sent from the client when a player clicks certain options on an
// * interface.
// * @author lare96 <http://github.com/lare96>
// */
//public final class InterfaceClickPacket implements IncomingPacket {
//
//	@Override
//	public void handle(Player player, int opcode, int size, GamePacket buf) {
//		if(player.getActivityManager().contains(ActivityManager.ActivityType.INTERFACE_CLICK))
//			return;
//
//		if(ExchangeSessionManager.get().reset(player)) {
//			return;
//		}
//
//		MinigameHandler.executeVoid(player, t -> t.onInterfaceClick(player));
//		player.closeWidget();
//		player.getActivityManager().execute(ActivityManager.ActivityType.INTERFACE_CLICK);
//	}
//}
