package net.arrav.action.item;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ItemAction;
import net.arrav.content.minigame.barrows.BarrowsMinigame;
import net.arrav.task.LinkedTaskSequence;
import net.arrav.world.Animation;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.item.Item;
import net.arrav.world.entity.item.container.impl.Inventory;

public class Spade extends ActionInitializer {
	@Override
	public void init() {
		ItemAction e = new ItemAction() {
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
