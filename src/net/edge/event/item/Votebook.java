package net.edge.event.item;

import net.edge.content.VoteRewards;
import net.edge.event.EventInitializer;
import net.edge.event.impl.ItemEvent;
import net.edge.util.TextUtils;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.container.impl.Inventory;

public class Votebook extends EventInitializer {
	@Override
	public void init() {
		ItemEvent e = new ItemEvent() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				if(container != Inventory.INVENTORY_DISPLAY_ID)
					return true;
				Item reward = VoteRewards.getReward().orElse(null);
				player.getInventory().remove(item);
				if(reward == null) {
					player.message("You were unlucky and didn't receive a extra reward...");
					return true;
				}
				String name = reward.getDefinition().getName();
				player.message("You were lucky and received x" + item.getAmount() + " " + TextUtils.capitalize(name));
				player.getInventory().addOrBank(reward);
				return true;
			}
		};
		e.register(6829);
	}
}
