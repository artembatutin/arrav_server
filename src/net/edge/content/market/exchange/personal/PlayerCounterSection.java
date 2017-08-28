package net.edge.content.market.exchange.personal;

import net.edge.content.market.exchange.item.ExchangeItem;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.container.ItemContainer;

/**
 * A single section in the player counter.
 */
public abstract class PlayerCounterSection {

	/**
	 * An array of all items in this section.
	 */
	public ExchangeItem items[] = new ExchangeItem[40];

	/**
	 * The visual {@link ItemContainer} in this section.
	 */
	private ItemContainer container = new ItemContainer(40, ItemContainer.StackPolicy.ALWAYS);

	/**
	 * Updating an item in this section.
	 * @param player the hosted player.
	 * @param item   the item being updated.
	 */
	public abstract void update(Player player, ExchangeItem item);

	/**
	 * Removing an item in this section.
	 * @param player the hosted player.
	 * @param item   the item being removed.
	 */
	public abstract void remove(Player player, ExchangeItem item);

	/**
	 * Gets the array of all items.
	 * @return exchange item array.
	 */
	public ExchangeItem[] getItems() {
		return items;
	}

	/**
	 * Gets the item container.
	 * @return item container.
	 */
	public ItemContainer getContainer() {
		return container;
	}

	/**
	 * Gets the condition if the item is already in the section.
	 * @param item the item searching for.
	 * @return -1 if the item isn't in, otherwise returns the index.
	 */
	public int exists(ExchangeItem item) {
		for(int i = 0; i < items.length; i++) {
			if(items[i] == null)
				return -1;//doesn't exist.
			if(items[i].getId() == item.getId())
				return i;
		}
		return -1;
	}

	/**
	 * Updates the item container with the new item.
	 * @param i    the index in which it locates.
	 * @param item the item being updated.
	 */
	public void update(int i, ExchangeItem item) {
		container.set(i, new Item(item.getId(), item.getAmount()), true);
	}
}
