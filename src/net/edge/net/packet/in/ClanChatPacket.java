package net.edge.net.packet.in;

import net.edge.content.quest.QuestManager;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager;

/**
 * The message that is sent from the client when the player chats anything
 * beginning with '/'.
 * @author Artem batutin <artembatutin@gmail.com>
 */
public final class ClanChatPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.CHAT_MESSAGE))
			return;
		String input = payload.getCString();
		if(input.equalsIgnoreCase("may luck be yours on halloween.") || input.equalsIgnoreCase("may luck be yours on halloween")) {
			player.getQuestManager().start(QuestManager.Quests.HALLOWEEN);
			return;
		}
		if(player.getClan().isPresent()) {
			player.getClan().get().message(input);
		} else {
			player.message("You're not in a clan.");
		}
		player.getActivityManager().execute(ActivityManager.ActivityType.CHAT_MESSAGE);
	}
}
