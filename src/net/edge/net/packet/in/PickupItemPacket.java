package net.edge.net.packet.in;

import net.edge.content.market.MarketItem;
import net.edge.content.minigame.MinigameHandler;
import net.edge.net.codec.ByteOrder;
import net.edge.net.codec.IncomingMsg;
import net.edge.net.packet.IncomingPacket;
import net.edge.util.log.impl.DropItemLog;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.activity.ActivityManager;
import net.edge.world.entity.item.GroundItem;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.region.Region;
import net.edge.world.locale.Position;

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
		Position position = new Position(itemX, itemY, player.getPosition().getZ());
		Optional<Region> reg = World.getRegions().getRegion(position);
		if(reg.isPresent()) {
			Optional<GroundItem> item = reg.get().getItem(itemId, position);
			item.ifPresent(groundItem -> player.getMovementListener().append(() -> {
				if(player.getPosition().same(new Position(itemX, itemY, player.getPosition().getZ()))) {
					if(!MinigameHandler.execute(player, m -> m.canPickup(player, groundItem))) {
						return;
					}
					if(!player.getInventory().hasCapacityFor(new Item(itemId, groundItem.getItem().getAmount()))) {
						player.message("You don't have enough inventory space to pick this item up.");
						return;
					}
					if(groundItem.getPlayer() != null) {
						int val = MarketItem.get(groundItem.getItem().getId()) != null ? MarketItem.get(groundItem.getItem().getId()).getPrice() * groundItem.getItem().getAmount() : 0;
						if(val > 5_000)
							World.getLoggingManager().write(new DropItemLog(player, groundItem.getItem(), player.getPosition(), Optional.of(groundItem)));
					}
					groundItem.onPickup(player);
					MinigameHandler.executeVoid(player, m -> m.onPickup(player, groundItem.getItem()));
				}
			}));
		}
		player.getActivityManager().execute(ActivityManager.ActivityType.PICKUP_ITEM);
	}
}
