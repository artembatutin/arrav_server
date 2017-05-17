package net.edge.net.message.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.message.InputMessageListener;
import net.edge.world.content.commands.CommandDispatcher;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.player.assets.activity.ActivityManager;

/**
 * The message that is sent from the client when the player chats anything
 * beginning with '::'.
 * @author lare96 <http://github.com/lare96>
 */
public final class CommandMessage implements InputMessageListener {
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.COMMAND_MESSAGE))
			return;
		String command = payload.getString();
		String[] parts = command.toLowerCase().split(" ");
		CommandDispatcher.execute(player, parts, command);
		player.getActivityManager().execute(ActivityManager.ActivityType.COMMAND_MESSAGE);
	}
}
