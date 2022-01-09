//package com.rageps.net.packet.in;
//
//import com.rageps.net.codec.game.GamePacket;
//import com.rageps.net.packet.IncomingPacket;
//import com.rageps.world.entity.actor.player.Player;
//import com.rageps.world.entity.actor.player.assets.Rights;
//import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
//
///**
// * The message sent from the client when the player clicks some sort of button or
// * module.
// * @author lare96 <http://github.com/lare96>
// */
//public final class InterfaceActionPacket implements IncomingPacket {
//
//	@Override
//	public void handle(Player player, int opcode, int size, GamePacket buf) {
//		if(player.getActivityManager().contains(ActivityManager.ActivityType.INTERFACE_ACTION))
//			return;
//		int interfaceId = buf.getInt();
//		int action = buf.getInt();
//		/*if(player.getClan().isPresent()) {
//			player.getClan().get().getClan().getSettings().click(player.getClan().get(), interfaceId, action);
//		}
//		if(interfaceId >= 50144 && interfaceId <= 50244) {
//			if(player.getClan().isPresent()) {
//				if(action == 0) {
//					player.getClan().get().rank(interfaceId - 50144, 1);
//				} else if(action == 1) {
//					player.getClan().get().mute(interfaceId - 50144);
//				} else if(action == 2) {
//					player.getClan().get().ban(interfaceId - 50144);
//				}
//				return;
//			}
//		}
//		if(interfaceId >= 400 && interfaceId <= 500) {
//			if(player.getClan().isPresent()) {
//				player.getClan().get().rank(interfaceId - 400, action);
//				return;
//			}
//		}
//		if(interfaceId >= 600 && interfaceId <= 700) {
//			if(player.getClan().isPresent()) {
//				if(action == 1) {
//					player.getClan().get().unban(interfaceId - 600);
//				} else {
//					player.getClan().get().unmute(interfaceId - 600);
//				}
//			}
//		}
//		if(interfaceId == 701) {
//			if(action == 1) {
//				ClanManager.get().delete(player);
//			} else {
//				player.getClan().filter(c -> c.getRank() == ClanChatRank.OWNER).ifPresent(clan -> player.send(new EnterName("The new clan chat name to set:", s -> () -> {
//					if(!player.getClan().isPresent() || player.getClan().get().getRank() != ClanChatRank.OWNER) {
//						player.message("You are unable to do that.");
//					} else {
//						player.getClan().get().getClan().setName(TextUtils.capitalize(s));
//						ClanManager.get().update(ClanChatUpdate.NAME_MODIFICATION, player.getClan().get().getClan());
//					}
//				})));
//			}
//		}*/
//		switch(interfaceId) {
//
//			default:
//				if(player.getRights().greater(Rights.ADMINISTRATOR))
//					player.message("Interface: " + interfaceId + " - Action: " + action);
//				break;
//		}
//
//		player.getActivityManager().execute(ActivityManager.ActivityType.INTERFACE_ACTION);
//	}
//}
