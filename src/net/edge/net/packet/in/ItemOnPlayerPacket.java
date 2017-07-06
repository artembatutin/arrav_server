package net.edge.net.packet.in;

import net.edge.net.codec.IncomingMsg;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.ByteTransform;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.activity.ActivityManager;
import net.edge.world.node.item.Item;

/**
 * The message sent from the client when a player uses an item on another player.
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemOnPlayerPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_ON_PLAYER))
			return;
		
		int container = payload.getShort(ByteTransform.A, ByteOrder.BIG);
		int index = payload.getShort();
		int itemUsed = payload.getShort();
		int itemSlot = payload.getShort(false, ByteTransform.A, ByteOrder.LITTLE);
		Item item = player.getInventory().get(itemSlot);
		Player usedOn = World.get().getPlayers().get(index - 1);
		
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
