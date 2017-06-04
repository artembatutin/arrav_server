package net.edge.net.packet.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.net.codec.ByteOrder;
import net.edge.net.packet.PacketReader;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.activity.ActivityManager;

/**
 * The message sent from the client when a player tries to follow another player.
 * @author lare96 <http://github.com/lare96>
 */
public final class FollowPlayerPacket implements PacketReader {
	
	@Override
	public void handle(Player player, int opcode, int size, ByteMessage payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.FOLLOW_PLAYER))
			return;
		int index = payload.getShort(false, ByteOrder.LITTLE);
		Player follow = World.get().getPlayers().get(index - 1);
		
		if(follow == null || !follow.getPosition().isViewableFrom(player.getPosition()) || follow.equals(player))
			return;
		player.getMovementQueue().follow(follow);
		player.getActivityManager().execute(ActivityManager.ActivityType.FOLLOW_PLAYER);
	}
}
