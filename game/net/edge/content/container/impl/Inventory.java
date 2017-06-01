package net.edge.content.container.impl;

import net.edge.content.container.ItemContainer;
import net.edge.content.container.ItemContainerAdapter;
import net.edge.content.container.ItemWeightListener;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;
import net.edge.world.node.item.ItemNode;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * An {@link ItemContainer} implementation that manages the inventory for a {@link Player}.
 * @author lare96 <http://github.com/lare96>
 */
public final class Inventory extends ItemContainer {
	
	@Override
	public ItemContainer copy() {
		return new Inventory(player);
	}
	
	/**
	 * An {@link ItemContainerAdapter} implementation that listens for changes to the inventory.
	 */
	private final class InventoryListener extends ItemContainerAdapter {
		
		/**
		 * Creates a new {@link InventoryListener}.
		 */
		InventoryListener(Player player) {
			super(player);
		}
		
		@Override
		public int getWidgetId() {
			return INVENTORY_DISPLAY_ID;
		}
		
		@Override
		public String getCapacityExceededMsg() {
			return "You do not have enough space in your inventory.";
		}
	}
	
	/**
	 * The inventory item display widget identifier.
	 */
	public static final int INVENTORY_DISPLAY_ID = 3214;
	
	/**
	 * The player instance for which this inventory applies to.
	 */
	private final Player player;
	
	/**
	 * Creates a new {@link Inventory}.
	 * @param player The {@link Player} this instance is dedicated to.
	 */
	public Inventory(Player player) {
		super(28, StackPolicy.STANDARD);
		addListener(new InventoryListener(player));
		addListener(new ItemWeightListener(player));
		this.player = player;
	}
	
	/**
	 * Attempts to add the {@code items} to the inventory, if inventory is full
	 * it'll execute the {@code action} for the remaining items that were not added.
	 * @param action  the action to execute if the item couldn't be added.
	 * @param message the optional message to sent if the items were not added.
	 * @param items   the items to be added.
	 */
	public void addOrExecute(Consumer<Item> action, Optional<String> message, List<Item> items) {
		boolean val = false;
		for(Item item : items) {
			if(item == null)
				continue;
			if(hasCapacityFor(item)) {
				player.getInventory().add(item);
			} else {
				action.accept(item);
				val = true;
			}
		}
		if(val) {
			message.ifPresent(player::message);
		}
	}
	
	/**
	 * Attempts to add the {@code items} to the inventory, if inventory is full
	 * it'll execute the {@code action} for the remaining items that were not added.
	 * @param action  the action to execute if the item couldn't be added.
	 * @param message the optional message to sent if the items were not added.
	 * @param items   the items to be added.
	 */
	public void addOrExecute(Consumer<Item> action, Optional<String> message, Item... items) {
		addOrExecute(action, message, Arrays.asList(items));
	}
	
	/**
	 * Attempts to add the {@code items} to the inventory, if inventory is full
	 * it'll execute the {@code action} for the remaining items that were not added.
	 * @param action  the action to execute if the item couldn't be added.
	 * @param message the message to be sent if some of the items were not added.
	 * @param items   the items to be added.
	 */
	public void addOrExecute(Consumer<Item> action, String message, Item... items) {
		addOrExecute(action, Optional.of(message), Arrays.asList(items));
	}
	
	/**
	 * Attempts to add the {@code items} to the inventory, if inventory is full
	 * it'll execute the {@code action} for the remaining items that were not added.
	 * @param action  the action to execute if the item couldn't be added.
	 * @param message the message to be sent if some of the items were not added.
	 * @param items   the items to be added.
	 */
	public void addOrExecute(Consumer<Item> action, String message, List<Item> items) {
		addOrExecute(action, Optional.of(message), items);
	}
	
	/**
	 * Attempts to add the {@code items} to the inventory, if inventory is full
	 * it'll execute the {@code action} for the remaining items that were not added.
	 * @param action the action to execute if the item couldn't be added.
	 * @param items  the items to be added.
	 */
	public void addOrExecute(Consumer<Item> action, List<Item> items) {
		addOrExecute(action, Optional.empty(), items);
	}
	
	/**
	 * Attempts to add the {@code items} to the inventory, if inventory is full
	 * it'll execute the {@code action} for the remaining items that were not added.
	 * @param action the action to execute if the item couldn't be added.
	 * @param items  the items to be added.
	 */
	public void addOrExecute(Consumer<Item> action, Item... items) {
		addOrExecute(action, Arrays.asList(items));
	}
	
	/**
	 * Attempts to add an item to the players inventory, if there is no space
	 * it'll bank the item instead.
	 * @param items the items to add.
	 * @return <true> if the item was added to the inventory or bank, <false> if even the bank
	 * had no space.
	 */
	public void addOrDrop(List<Item> items) {
		addOrExecute(t -> {
			ItemNode node = new ItemNode(t, player.getPosition(), player);
			player.getRegion().register(node);
		}, "Some of the items were dropped beneath you instead...", items);
	}
	
	/**
	 * Attempts to add an item to the players inventory, if there is no space
	 * it'll bank the item instead.
	 * @param items the items to add.
	 * @return <true> if the item was added to the inventory or bank, <false> if even the bank
	 * had no space.
	 */
	public void addOrDrop(Item... items) {
		addOrDrop(Arrays.asList(items));
	}
	
	/**
	 * Attempts to add an item to the players inventory, if there is no space
	 * it'll bank the item instead.
	 * @param items the items to add.
	 * @return <true> if the item was added to the inventory or bank, <false> if even the bank
	 * had no space.
	 */
	public void addOrBank(List<Item> items) {
		addOrExecute(t -> player.getBank().add(0, t), "Some of the items were banked instead...", items);
	}
	
	/**
	 * Attempts to add an item to the players inventory, if there is no space
	 * it'll bank the item instead.
	 * @param items the items to add.
	 * @return <true> if the item was added to the inventory or bank, <false> if even the bank
	 * had no space.
	 */
	public void addOrBank(Item... items) {
		addOrBank(Arrays.asList(items));
	}
}