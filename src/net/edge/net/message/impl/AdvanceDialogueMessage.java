package net.edge.net.message.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.message.InputMessageListener;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.player.assets.activity.ActivityManager.ActivityType;

/**
 * The message sent from the client when the player clicks on the 'Click this to
 * continue' link to forward a dialogue.
 * @author lare96 <http://github.com/lare96>
 */
public final class AdvanceDialogueMessage implements InputMessageListener {
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
		if(player.getActivityManager().contains(ActivityType.DIALOGUE_INTERACTION))
			return;
		player.getDialogueBuilder().advance();
		player.getActivityManager().execute(ActivityType.DIALOGUE_INTERACTION);
	}
}
