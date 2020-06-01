package com.rageps.net.packet.in;

import com.rageps.content.market.MarketItem;
import com.rageps.content.minigame.MinigameHandler;
import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.IncomingPacket;
import com.rageps.util.log.impl.DropItemLog;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.item.GroundItem;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.region.Region;
import com.rageps.world.locale.Position;

import java.util.Optional;

/**
 * The message sent from the client when a player attempts to pick up an item.
 * @author lare96 <http://github.com/lare96>
 */
public final class PickupItemPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, GamePacket buf) {
		if(player.getActivityManager().contains(ActivityManager.ActivityType.PICKUP_ITEM))
			return;
		int itemY = buf.getShort(ByteOrder.LITTLE);
		int itemId = buf.getShort(false);
		int itemX = buf.getShort(ByteOrder.LITTLE);
		if(itemY < 0 || itemId < 0 || itemX < 0)
			return;
		Position position = new Position(itemX, itemY, player.getPosition().getZ());
		Region reg = World.getRegions().getRegion(position);
		if(reg != null) {
			Optional<GroundItem> item = reg.getItem(itemId, position);
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
