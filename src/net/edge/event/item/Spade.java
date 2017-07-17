package net.edge.event.item;

import net.edge.content.minigame.barrows.BarrowsMinigame;
import net.edge.event.EventInitializer;
import net.edge.event.impl.ItemEvent;
import net.edge.task.LinkedTaskSequence;
import net.edge.world.Animation;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.container.impl.Inventory;

public class Spade extends EventInitializer {
	@Override
	public void init() {
		ItemEvent e = new ItemEvent() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				if(container != Inventory.INVENTORY_DISPLAY_ID)
					return true;
				LinkedTaskSequence seq = new LinkedTaskSequence();
				seq.connect(1, () -> player.animation(new Animation(830)));
				seq.connect(3, () -> {
					if(BarrowsMinigame.dig(player)) {
						return;
					}
					player.message("You found nothing interesting...");
				});
				seq.start();
				return true;
			}
		};
		e.register(952);
	}
}
