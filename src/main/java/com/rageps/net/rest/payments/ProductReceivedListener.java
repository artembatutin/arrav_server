package com.rageps.net.rest.payments;


import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

/**
 * Created by Ryley Kimmel on 10/30/2016.
 */
public interface ProductReceivedListener {
	boolean receive(Player player, Product product);

	default void add(Player player, Item item) {
		if(player.getInventory().canAdd(item)) {
			//player.message(ChatColor.RED + "Your " + item.getDefinition().getName() + " has been added to your inventory.");
			player.getInventory().add(item);
			return;
		}

		if (player.getBank().container(0).canAdd(item)) {
			//player.message(ChatColor.RED + "Your " + item.getDefinition().getName() + " has been added to your bank.");
			player.getBank().deposit(item);
			return;
		}

		if (!item.getDefinition().isStackable()) {
			for (int i = 0; i < item.getAmount(); i++) {
		//		GroundItemManager.spawnGroundItem(player, GroundItem.getStandard(new Item(item.getId(), 1), player.getPosition(), player));
			}
		} else {
		//	GroundItemManager.spawnGroundItem(player, GroundItem.getStandard(item, player.getPosition(), player));
		}

		//player.message(ChatColor.RED + "Your " + item.getDefinition().getName() + " has dropped on the floor below you.");
	}
}
