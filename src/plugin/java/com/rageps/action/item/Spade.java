package com.rageps.action.item;

import com.rageps.content.minigame.barrows.BarrowsMinigame;
import com.rageps.action.ActionInitializer;
import com.rageps.action.impl.ItemAction;
import com.rageps.task.LinkedTaskSequence;
import com.rageps.world.Animation;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.container.impl.Inventory;

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
