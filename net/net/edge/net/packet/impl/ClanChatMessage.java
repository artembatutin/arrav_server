package net.edge.net.packet.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.packet.PacketReader;
import net.edge.content.quest.QuestManager;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.activity.ActivityManager;

/**
 * The message that is sent from the client when the player chats anything
 * beginning with '/'.
 * @author Artem batutin <artembatutin@gmail.com>
 */
public final class ClanChatMessage implements PacketReader {
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.CHAT_MESSAGE))
			return;
		String input = payload.getString();
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
