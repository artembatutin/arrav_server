package net.arrav.action.item;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ItemAction;
import net.arrav.content.skill.runecrafting.Runecrafting;
import net.arrav.content.skill.runecrafting.pouch.PouchType;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.item.container.impl.Inventory;

public class EssencePouch extends ActionInitializer {
	@Override
	public void init() {
		ItemAction e = new ItemAction() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				if(container != Inventory.INVENTORY_DISPLAY_ID)
					return true;
				Runecrafting.fill(player, PouchType.SMALL);
				return true;
			}
		};
		e.register(5509);
		e = new ItemAction() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				if(container != Inventory.INVENTORY_DISPLAY_ID)
					return true;
				Runecrafting.fill(player, PouchType.MEDIUM);
				return true;
			}
		};
		e.register(5510);
		e = new ItemAction() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				if(container != Inventory.INVENTORY_DISPLAY_ID)
					return true;
				Runecrafting.fill(player, PouchType.LARGE);
				return true;
			}
		};
		e.register(5512);
		e = new ItemAction() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				if(container != Inventory.INVENTORY_DISPLAY_ID)
					return true;
				Runecrafting.fill(player, PouchType.GIANT);
				return true;
			}
		};
		e.register(5514);
	}
}
