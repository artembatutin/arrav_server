package com.rageps.action.item;

import com.rageps.action.ActionInitializer;
import com.rageps.action.impl.ItemAction;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.container.impl.Inventory;

public class DonatorScrolls extends ActionInitializer {
	@Override
	public void init() {
		ItemAction e = new ItemAction() {
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
		e = new ItemAction() {
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
		e = new ItemAction() {
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
