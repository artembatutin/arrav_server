package net.edge.event.item;

import net.edge.event.EventInitializer;
import net.edge.event.impl.ItemEvent;
import net.edge.util.rand.RandomUtils;
import net.edge.world.node.entity.npc.drop.ItemCache;
import net.edge.world.node.entity.npc.drop.NpcDrop;
import net.edge.world.node.entity.npc.drop.NpcDropManager;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.container.impl.Inventory;

import static net.edge.world.node.entity.npc.drop.ItemCache.*;

public class Casket extends EventInitializer {
	@Override
	public void init() {
		ItemCache[] tables = {
				LOW_RUNES,
				LOW_HERBS,
				LOW_GEMS,
				CHARMS
		};
		ItemEvent e = new ItemEvent() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				if(container != Inventory.INVENTORY_DISPLAY_ID)
					return true;
				ItemCache table = RandomUtils.random(tables);
				player.getInventory().remove(item, slot);
				//three items.
				NpcDrop[] drop = {
						RandomUtils.random(NpcDropManager.COMMON.get(table)),
						RandomUtils.random(NpcDropManager.COMMON.get(table)),
						RandomUtils.random(NpcDropManager.COMMON.get(table))
				};
				Item[] items = {
						new Item(drop[0].getId(), RandomUtils.inclusive(drop[0].getMinimum(), drop[0].getMaximum())),
						new Item(drop[1].getId(), RandomUtils.inclusive(drop[1].getMinimum(), drop[1].getMaximum())),
						new Item(drop[2].getId(), RandomUtils.inclusive(drop[2].getMinimum(), drop[2].getMaximum())),
				};
				player.getInventory().addOrDrop(items);
				return true;
			}
		};
		e.register(405);
	}
}
