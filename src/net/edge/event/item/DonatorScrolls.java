package net.edge.event.item;

import net.edge.event.EventInitializer;
import net.edge.event.impl.ItemEvent;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.actor.player.assets.Rights;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.container.impl.Inventory;

public class DonatorScrolls extends EventInitializer {
	@Override
	public void init() {
		ItemEvent e = new ItemEvent() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				if(container != Inventory.INVENTORY_DISPLAY_ID)
					return true;
				player.setRights(Rights.DONATOR);
				player.message("You are now a donator.");
				player.getInventory().remove(new Item(692));
				return true;
			}
		};
		e.register(692);
		e = new ItemEvent() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				if(container != Inventory.INVENTORY_DISPLAY_ID)
					return true;
				player.setRights(Rights.SUPER_DONATOR);
				player.message("You are now a super donator.");
				player.getInventory().remove(new Item(693));
				return true;
			}
		};
		e.register(693);
		e = new ItemEvent() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				if(container != Inventory.INVENTORY_DISPLAY_ID)
					return true;
				player.setRights(Rights.EXTREME_DONATOR);
				player.message("You are now a extreme donator.");
				player.getInventory().remove(new Item(691));
				return true;
			}
		};
		e.register(691);
	}
}
