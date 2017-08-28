package net.edge.net.packet.in;

import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager.ActivityType;

/**
 * The message sent from the client when the player clicks on the 'Click this to
 * continue' link to forward a dialogue.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class AdvanceDialoguePacket implements IncomingPacket {

	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
		if(player.getActivityManager().contains(ActivityType.DIALOGUE_INTERACTION))
			return;
		player.getDialogueBuilder().advance();
		player.getActivityManager().execute(ActivityType.DIALOGUE_INTERACTION);
	}
}
