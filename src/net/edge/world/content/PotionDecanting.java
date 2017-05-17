package net.edge.world.content;

import net.edge.world.content.container.impl.Inventory;
import net.edge.world.content.item.PotionConsumable;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.model.node.item.ItemDefinition;

/**
 * The class which holds functionality for decanting potions.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class PotionDecanting {
	
	/**
	 * Holds support for manually decanting, this is done by mixing two potions of
	 * the same kind together.
	 * @param player the player who's decanting.
	 * @param used   the item that was used.
	 * @param usedOn the item the first item was used on.
	 * @return {@code true} if any items were decanted, {@code false} otherwise.
	 */
	public static boolean manual(Player player, Item used, Item usedOn) {
		PotionConsumable first = PotionConsumable.forId(used.getId()).orElse(null);
		
		if(first == null) {
			return false;
		}
		
		PotionConsumable second = PotionConsumable.forId(usedOn.getId()).orElse(null);
		
		if(second == null) {
			return false;
		}
		
		if(!first.equals(second)) {
			player.message("You can't combine different kind of potions.");
			return false;
		}
		
		if(first.getIds()[0] == used.getId() || second.getIds()[0] == usedOn.getId()) {
			player.message("You can't combine these potions as one of them is already full.");
			return false;
		}
		
		final int doses = getDoses(used.getId()) + getDoses(usedOn.getId());
		
		if(doses <= (first.getIds().length + 1)) {
			player.getInventory().replace(used.getId(), first.getIdForDose(doses), false);
			player.getInventory().replace(usedOn.getId(), 229, false);
		} else {
			player.getInventory().replace(used.getId(), first.getIds()[0], false);
			player.getInventory().replace(usedOn.getId(), second.getIdForDose(doses - 4), false);
		}
		
		player.getInventory().refresh(player, Inventory.INVENTORY_DISPLAY_ID);
		return true;
	}
	
	/**
	 * Calculates the amount of doses the specified item {@code id} contains.
	 * @param id the id to calculate the doses from.
	 * @return a numerical value representing the amount of the dose.
	 */
	private static int getDoses(int id) {
		ItemDefinition definition = ItemDefinition.get(id);
		int index = definition.getName().lastIndexOf(')');
		return Integer.valueOf(definition.getName().substring(index - 1, index));
	}
}
