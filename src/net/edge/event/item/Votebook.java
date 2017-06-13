package net.edge.event.item;

import net.edge.content.VoteRewards;
import net.edge.event.EventInitializer;
import net.edge.event.impl.ItemEvent;
import net.edge.util.TextUtils;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

public class Votebook extends EventInitializer {
	@Override
	public void init() {
		ItemEvent e = new ItemEvent() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				if(click != 1) {
					return true;
				}

				Item reward = VoteRewards.getReward().orElse(null);

				if(reward == null) {
					player.message("You were unlucky and didn't receive a extra reward...");
					return true;
				}

				String name = reward.getDefinition().getName();
				player.message("You were lucky and received x" + item.getAmount() + " " + TextUtils.capitalize(name));
				player.getInventory().remove(item);
				player.getInventory().addOrBank(reward);
				return true;
			}
		};
		e.registerInventory(6829);
	}
}
