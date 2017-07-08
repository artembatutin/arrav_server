package net.edge.event.item;

import net.edge.event.EventInitializer;
import net.edge.event.impl.ItemEvent;
import net.edge.util.rand.RandomUtils;
import net.edge.world.node.entity.npc.drop.ItemCache;
import net.edge.world.node.entity.npc.drop.NpcDrop;
import net.edge.world.node.entity.npc.drop.NpcDropManager;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.container.impl.Inventory;

import static net.edge.world.node.entity.npc.drop.ItemCache.*;

public class Casket extends EventInitializer {
	@Override
	public void init() {
		ItemCache[] tables = {
				HERB_SEEDS,
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
						RandomUtils.random(NpcDropManager.COMMON.get(table))
				};
				int coints = (int) (200_000 * (player.getRights() == Rights.EXTREME_DONATOR ? 2 : player.getRights() == Rights.SUPER_DONATOR ? 1.5 : player.getRights() == Rights.DONATOR ? 1.3 : 1));
				Item[] items = {
						new Item(drop[2].getId(), RandomUtils.inclusive(drop[2].getMinimum(), drop[2].getMaximum())),
						new Item(995, RandomUtils.inclusive(400, coints))
				};
				player.getInventory().addOrDrop(items);
				return true;
			}
		};
		e.register(405);
	}
}
