package com.rageps.net.packet.in;

import com.rageps.content.quest.QuestManager;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.IncomingPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;

/**
 * The message that is sent from the client when the player chats anything
 * beginning with '/'.
 * @author Artem Batutin
 */
public final class ClanChatPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, GamePacket buf) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.CHAT_MESSAGE))
			return;
		String input = buf.getCString();
		if(input.equalsIgnoreCase("may luck be yours on halloween.") || input.equalsIgnoreCase("may luck be yours on halloween")) {
			player.getQuestManager().start(QuestManager.Quests.HALLOWEEN);
			return;
		}
		if(player.getClan().isPresent()) {
			if(player.muted || player.ipMuted) {
				player.message("You are muted and unable to talk!");
				return;
			}
			player.getClan().get().message(input);
		} else {
			player.message("You're not in a clan.");
		}
		player.getActivityManager().execute(ActivityManager.ActivityType.CHAT_MESSAGE);
	}
}
