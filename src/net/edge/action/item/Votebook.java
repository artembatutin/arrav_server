package net.edge.action.item;

import net.edge.content.VoteRewards;
import net.edge.action.ActionInitializer;
import net.edge.action.impl.ItemAction;
import net.edge.util.TextUtils;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.container.impl.Inventory;

public class Votebook extends ActionInitializer {
	@Override
	public void init() {
		ItemAction e = new ItemAction() {
			@Override
			public boolean click(Player player, Item item, int container, int slot, int click) {
				if(container != Inventory.INVENTORY_DISPLAY_ID)
					return true;
				player.setVotePoints(1);
				player.setTotalVotes(1);
				player.message("You received a vote point, you can access the vote shop in the quest tab.");
				Item reward = VoteRewards.getReward().orElse(null);
				player.getInventory().remove(item);
				if(reward == null) {
					player.message("... but you were unlucky and didn't receive a extra reward.");
					return true;
				}
				String name = reward.getDefinition().getName();
				player.message("... and you were lucky and received x" + item.getAmount() + " " + TextUtils.capitalize(name) + ".");
				player.getInventory().addOrBank(reward);
				return true;
			}
		};
		e.register(6829);
	}
}
