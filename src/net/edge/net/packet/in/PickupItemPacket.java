package net.edge.net.packet.in;

import net.edge.content.minigame.MinigameHandler;
import net.edge.locale.Position;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.codec.ByteOrder;
import net.edge.net.packet.IncomingPacket;
import net.edge.world.World;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.actor.player.assets.activity.ActivityManager;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemNode;
import net.edge.world.node.region.Region;

import java.util.Optional;

/**
 * The message sent from the client when a player attempts to pick up an item.
 * @author lare96 <http://github.com/lare96>
 */
public final class PickupItemPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, IncomingMsg payload) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.PICKUP_ITEM))
			return;
		int itemY = payload.getShort(ByteOrder.LITTLE);
		int itemId = payload.getShort(false);
		int itemX = payload.getShort(ByteOrder.LITTLE);
		if(itemY < 0 || itemId < 0 || itemX < 0)
			return;
		player.getMovementListener().append(() -> {
			if(player.getPosition().same(new Position(itemX, itemY, player.getPosition().getZ()))) {
				Position position = new Position(itemX, itemY, player.getPosition().getZ());
				Region region = World.getRegions().getRegion(position);
				if(region == null)
					return;
				Optional<ItemNode> item = region.getItem(itemId, position);
				if(item.isPresent()) {
					if(!MinigameHandler.execute(player, m -> m.canPickup(player, item.get()))) {
						return;
					}
					if(!player.getInventory().hasCapacityFor(new Item(itemId, item.get().getItem().getAmount()))) {
						player.message("You don't have enough inventory space to pick this item up.");
						return;
					}
					item.get().onPickup(player);
					MinigameHandler.executeVoid(player, m -> m.onPickup(player, item.get().getItem()));
				}
			}
		});
		player.getActivityManager().execute(ActivityManager.ActivityType.PICKUP_ITEM);
	}
}
