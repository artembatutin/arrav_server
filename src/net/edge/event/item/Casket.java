package net.edge.event.item;

import net.edge.event.EventInitializer;
import net.edge.event.impl.ItemEvent;
import net.edge.util.rand.RandomUtils;
import net.edge.world.node.entity.npc.drop.NpcDrop;
import net.edge.world.node.entity.npc.drop.NpcDropCache;
import net.edge.world.node.entity.npc.drop.NpcDropManager;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

import static net.edge.world.node.entity.npc.drop.NpcDropCache.*;

public class Casket extends EventInitializer {
	@Override
	public void init() {
		NpcDropCache[] tables = {
				LOW_RUNES,
				MED_RUNES,
				HIGH_RUNES,
				LOW_HERBS,
				MED_HERBS,
				HIGH_HERBS,
				LOW_GEMS,
				MED_GEMS,
				HIGH_GEMS,
				CHARMS
		};
		ItemEvent e = new ItemEvent() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				NpcDropCache table = RandomUtils.random(tables);
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
		e.registerInventory(405);
	}
}
