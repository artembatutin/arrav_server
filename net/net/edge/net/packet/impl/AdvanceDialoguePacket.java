package net.edge.net.packet.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.packet.PacketReader;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.activity.ActivityManager.ActivityType;

/**
 * The message sent from the client when the player clicks on the 'Click this to
 * continue' link to forward a dialogue.
 * @author lare96 <http://github.com/lare96>
 */
public final class AdvanceDialoguePacket implements PacketReader {
	
	@Override
	public void handle(Player player, int opcode, int size, ByteMessage payload) {
		if(player.getActivityManager().contains(ActivityType.DIALOGUE_INTERACTION))
			return;
		player.getDialogueBuilder().advance();
		player.getActivityManager().execute(ActivityType.DIALOGUE_INTERACTION);
	}
}
