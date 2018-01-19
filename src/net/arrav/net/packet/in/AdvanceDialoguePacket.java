package net.arrav.net.packet.in;

import io.netty.buffer.ByteBuf;
import net.arrav.net.packet.IncomingPacket;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.activity.ActivityManager.ActivityType;

/**
 * The message sent from the client when the player clicks on the 'Click this to
 * continue' link to forward a dialogue.
 * @author lare96 <http://github.com/lare96>
 */
public final class AdvanceDialoguePacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, ByteBuf buf) {
		if(player.getActivityManager().contains(ActivityType.DIALOGUE_INTERACTION))
			return;
		player.getDialogueBuilder().advance();
		player.getActivityManager().execute(ActivityType.DIALOGUE_INTERACTION);
	}
}
