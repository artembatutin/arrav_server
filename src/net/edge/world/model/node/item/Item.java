package net.edge.world.model.node.item;

import com.google.common.collect.Iterables;
import net.edge.world.content.market.MarketItem;

import java.util.ArrayList;
import java.util.List;

/**
 * The container class that represents an item that can be interacted with.
 * @author lare96 <http://github.com/lare96>
 */
public final class Item {
	
	/**
	 * The identification of this item.
	 */
	private int id;
	
	/**
	 * The quantity of this item.
	 */
	private int amount;
	
	/**
	 * Creates a new {@link Item}.
	 * @param id     the identification of this item.
	 * @param amount the quantity of this item.
	 */
	public Item(int id, int amount) {
		if(amount < 0)
			amount = 0;
		this.id = id;
		this.amount = amount;
	}
	
	/**
	 * Creates a new item with {@code newId} and the same amount as this instance. The returned {@code Item} <strong>does
	 * not</strong> hold any references to this one unless {@code id == newId}. It will throw an exception on an invalid id.
	 * @param newId The new id to set.
	 * @return The newly id set {@code Item}.
	 */
	public Item createWithId(int newId) {
		if(id == newId) {
			return this;
		}
		return new Item(newId, amount);
	}
	
	/**
	 * Creates a new item with {@code newAmount} and the same identifier as this instance.  The returned {@code Item}
	 * <strong>does not</strong> hold any references to this one unless {@code amount == newAmount}. It will throw an
	 * exception on overflows and negative values.
	 * @param newAmount The new amount to set.
	 * @return The newly amount set {@code Item}.
	 */
	public Item createWithAmount(int newAmount) {
		if(amount == newAmount) {
			return this;
		}
		return new Item(id, newAmount);
	}
	
	/**
	 * Creates a new item with {@code amount + addAmount} and the same identifier. The returned {@code Item} <strong>does
	 * not</strong> hold any references to this one. It will also have a maximum amount of {@code Integer.MAX_VALUE}.
	 * @param addAmount The amount to add.
	 * @return The newly incremented {@code Item}.
	 */
	public Item createAndIncrement(int addAmount) {
		if(addAmount < 0) { // Same effect as decrementing.
			return createAndDecrement(Math.abs(addAmount));
		}
		
		int newAmount = amount + addAmount;
		
		if(newAmount < amount) { // An overflow.
			newAmount = Integer.MAX_VALUE;
		}
		return new Item(id, newAmount);
	}
	
	/**
	 * Creates a new item with {@code amount - removeAmount} and the same identifier. The returned {@code Item} <strong>does
	 * not</strong> hold any references to this one. It will also have a minimum amount of {@code 1}.
	 * @param removeAmount The amount to remove.
	 * @return The newly incremented {@code Item}.
	 */
	public Item createAndDecrement(int removeAmount) {
		if(removeAmount < 0) { // Same effect as incrementing.
			return createAndIncrement(Math.abs(removeAmount));
		}
		
		int newAmount = amount - removeAmount;
		
		// Value too low, or an overflow.
		if(newAmount < 1 || newAmount > amount) {
			newAmount = 1;
		}
		return new Item(id, newAmount);
	}
	
	/**
	 * Creates a new {@link Item} with an quantity of {@code 1}.
	 * @param id the identification of this item.
	 */
	public Item(int id) {
		this(id, 1);
	}
	
	/**
	 * Converts an {@link Item} array into an Integer array.
	 * @param ids the array to convert into an Integer array.
	 * @return the Integer array containing the values from the item array.
	 */
	public static final int[] convert(Item... ids) {
		List<Integer> values = new ArrayList<>();
		for(Item identifier : ids) {
			values.add(identifier.getId());
		}
		return values.stream().mapToInt(Integer::intValue).toArray();
	}
	
	/**
	 * Converts an int array into an {@link Item} array.
	 * @param id the array to convert into an item array.
	 * @return the item array containing the values from the int array.
	 */
	public static final Item[] convert(int... id) {
		List<Item> items = new ArrayList<>();
		for(int identifier : id) {
			items.add(new Item(identifier));
		}
		return Iterables.toArray(items, Item.class);
	}
	
	/**
	 * Determines if {@code item} is valid. In other words, determines if
	 * {@code item} is not {@code null} and the {@link Item#id} and
	 * {@link Item#amount} are above {@code 0}.
	 * @param item the item to determine if valid.
	 * @return {@code true} if the item is valid, {@code false} otherwise.
	 */
	public static boolean valid(Item item) {
		return item != null && item.id > 0 && item.amount > 0 && item.id < ItemDefinition.DEFINITIONS.length && item.getDefinition() != null;
	}
	
	/**
	 * A substitute for {@link Object#clone()} that creates another 'copy' of
	 * this instance. The created copy <i>safe</i> meaning it does not hold
	 * <b>any</b> references to the original instance.
	 * @return the copy of this instance that does not hold any references.
	 */
	public Item copy() {
		return new Item(id, amount);
	}
	
	/**
	 * Increments the amount by {@code 1}.
	 */
	public final void incrementAmount() {
		incrementAmountBy(1);
	}
	
	/**
	 * Decrements the amount by {@code 1}.
	 */
	public final void decrementAmount() {
		decrementAmountBy(1);
	}
	
	/**
	 * Increments the amount by {@code amount}.
	 * @param amount the amount to increment by.
	 */
	public final void incrementAmountBy(int amount) {
		this.amount += amount;
	}
	
	/**
	 * Decrements the amount by {@code amount}
	 * @param amount the amount to decrement by.
	 */
	public final void decrementAmountBy(int amount) {
		if((this.amount - amount) < 1) {
			this.amount = 0;
		} else {
			this.amount -= amount;
		}
	}
	
	/**
	 * Gets the item definition for the item identifier.
	 * @return the item definition.
	 */
	public final ItemDefinition getDefinition() {
		return ItemDefinition.DEFINITIONS[id];
	}
	
	/**
	 * Gets the item value for the item identifier.
	 * @return the item value.
	 */
	public final MarketItem getValue() {
		return MarketItem.VALUES[id];
	}
	
	/**
	 * Gets the identification of this item.
	 * @return the identification.
	 */
	public final int getId() {
		return id;
	}
	
	/**
	 * Sets the identification of this item.
	 * @param id the new identification of this item.
	 */
	public final void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the quantity of this item.
	 * @return the quantity.
	 */
	public final int getAmount() {
		return amount;
	}
	
	/**
	 * Sets the quantity of this item.
	 * @param amount the new quantity of this item.
	 */
	public final void setAmount(int amount) {
		if(amount < 0)
			amount = 0;
		this.amount = amount;
	}
	
	@Override
	public final String toString() {
		return "ITEM[id= " + id + ", amount= " + amount + "]";
	}
	
	public final boolean equals(Item item) {
		return this == item || item != null && amount == item.amount && id == item.id;
	}
	
}