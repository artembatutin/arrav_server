package net.edge.net.packet.in;

import net.edge.net.codec.IncomingMsg;
import net.edge.net.codec.ByteOrder;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager;

/**
 * The message sent from the client when a player tries to follow another player.
 * @author lare96 <http://github.com/lare96>
 */
public final class FollowPlayerPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.FOLLOW_PLAYER))
			return;
		int index = payload.getShort(false, ByteOrder.LITTLE);
		Player follow = World.get().getPlayers().get(index - 1);
		
		if(follow == null || !follow.getPosition().isViewableFrom(player.getPosition()) || follow.same(player))
			return;
		player.getMovementQueue().follow(follow);
		player.getActivityManager().execute(ActivityManager.ActivityType.FOLLOW_PLAYER);
	}
}
