package net.edge.net.message.impl;

import net.edge.net.codec.ByteMessage;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.player.assets.activity.ActivityManager;
import net.edge.world.model.node.item.Item;
import net.edge.world.World;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.message.InputMessageListener;

/**
 * The message sent from the client when a player uses an item on another player.
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemOnPlayerMessage implements InputMessageListener {
	
	@Override
	public void handleMessage(Player player, int opcode, int size, ByteMessage payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_ON_PLAYER))
			return;
		
		int container = payload.getShort(ByteTransform.A, ByteOrder.BIG);
		int index = payload.getShort();
		int itemUsed = payload.getShort();
		int itemSlot = payload.getShort(false, ByteTransform.A, ByteOrder.LITTLE);
		Item item = player.getInventory().get(itemSlot);
		Player usedOn = World.getPlayers().get(index);
		
		if(container < 0 || item == null || usedOn == null || itemUsed < 0)
			return;
		if(item.getId() != itemUsed)
			return;
		
		player.getMovementListener().append(() -> {
			if(player.getPosition().withinDistance(usedOn.getPosition(), 1)) {
				
			}
		});
		player.getActivityManager().execute(ActivityManager.ActivityType.ITEM_ON_PLAYER);
	}
}
