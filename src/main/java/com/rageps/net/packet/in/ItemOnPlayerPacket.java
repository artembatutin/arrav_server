//package com.rageps.net.packet.in;
//
//import com.rageps.net.codec.ByteOrder;
//import com.rageps.net.codec.ByteTransform;
//import com.rageps.net.codec.game.GamePacket;
//import com.rageps.net.packet.IncomingPacket;
//import com.rageps.world.World;
//import com.rageps.world.entity.actor.player.Player;
//import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
//import com.rageps.world.entity.item.Item;
//
///**
// * The message sent from the client when a player uses an item on another player.
// * @author lare96 <http://github.com/lare96>
// */
//public final class ItemOnPlayerPacket implements IncomingPacket {
//
//	@Override
//	public void handle(Player player, int opcode, int size, GamePacket buf) {
//		if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_ON_PLAYER))
//			return;
//
//		int container = buf.getShort(ByteTransform.A, ByteOrder.BIG);
//		int index = buf.getShort();
//		int itemUsed = buf.getShort();
//		int itemSlot = buf.getShort(false, ByteTransform.A, ByteOrder.LITTLE);
//		Item item = player.getInventory().get(itemSlot);
//		Player usedOn = World.get().getPlayers().get(index - 1);
//
//		if(container < 0 || item == null || usedOn == null || itemUsed < 0)
//			return;
//		if(item.getId() != itemUsed)
//			return;
//
//		player.getMovementListener().append(() -> {
//			if(player.getPosition().withinDistance(usedOn.getPosition(), 1)) {
//
//			}
//		});
//		player.getActivityManager().execute(ActivityManager.ActivityType.ITEM_ON_PLAYER);
//	}
//}
