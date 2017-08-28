package net.edge.action.item;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.ItemAction;
import net.edge.content.skill.runecrafting.Runecrafting;
import net.edge.content.skill.runecrafting.pouch.PouchType;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.container.impl.Inventory;

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
