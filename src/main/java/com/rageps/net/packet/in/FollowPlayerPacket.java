//package com.rageps.net.packet.in;
//
//import com.rageps.net.codec.ByteOrder;
//import com.rageps.net.codec.game.GamePacket;
//import com.rageps.net.packet.IncomingPacket;
//import com.rageps.world.World;
//import com.rageps.world.entity.actor.player.Player;
//import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
//
///**
// * The message sent from the client when a player tries to follow another player.
// * @author lare96 <http://github.com/lare96>
// */
//public final class FollowPlayerPacket implements IncomingPacket {
//
//	@Override
//	public void handle(Player player, int opcode, int size, GamePacket buf) {
//		if(player.getActivityManager().contains(ActivityManager.ActivityType.FOLLOW_PLAYER))
//			return;
//		int index = buf.getShort(false, ByteOrder.LITTLE);
//		Player follow = World.get().getPlayers().get(index - 1);
//
//		if(follow == null || !follow.getPosition().isViewableFrom(player.getPosition()) || follow.same(player))
//			return;
//		player.getMovementQueue().follow(follow);
//		player.getActivityManager().execute(ActivityManager.ActivityType.FOLLOW_PLAYER);
//	}
//}
