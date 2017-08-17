package net.edge.action.item;

import net.edge.content.skill.hunter.Hunter;
import net.edge.content.skill.hunter.trap.bird.BirdSnare;
import net.edge.content.skill.hunter.trap.mammal.BoxTrap;
import net.edge.action.ActionInitializer;
import net.edge.action.impl.ItemAction;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.container.impl.Inventory;

public class HunterKit extends ActionInitializer {
	@Override
	public void init() {
		ItemAction e = new ItemAction() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				if(container != Inventory.INVENTORY_DISPLAY_ID)
					return true;
//				if(player.getInventory().remaining() < (HunterKit.ITEMS.length - 2)) {//-2 because we remove the hunter kit toolbox before adding the items
//					player.message("You don't have enough inventory space to unlock the hunter kit...");
//					return true;
//				}
//				player.getInventory().remove(HunterKit.HUNTER_KIT);
//				player.getInventory().addAll(HunterKit.ITEMS);
				return true;
			}
		};
		e.register(11159);
		e = new ItemAction() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				if(container != Inventory.INVENTORY_DISPLAY_ID)
					return true;
				Hunter.lay(player, new BirdSnare(player));
				return true;
			}
		};
		e.register(10006);
		e = new ItemAction() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				if(container != Inventory.INVENTORY_DISPLAY_ID)
					return true;
				Hunter.lay(player, new BoxTrap(player));
				return true;
			}
		};
		e.register(10008);
	}
}
