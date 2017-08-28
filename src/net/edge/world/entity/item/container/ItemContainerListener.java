package net.edge.world.entity.item.container;

import net.edge.world.entity.item.Item;

/**
 * A listener that is fired by {@link ItemContainer}. One should aim to extend {@link ItemContainerAdapter} for generic use
 * cases rather than implement this directly.
 * @author lare96 <http://github.org/lare96>
 */
public interface ItemContainerListener {
	
	/**
	 * Fired when an {@link Item} is added, removed, or replaced.
	 * @param container The {@link ItemContainer} firing the event.
	 * @param oldItem   The old item being removed from this container.
	 * @param newItem   The new item being added to this container.
	 * @param slot      The slot the update is occurring on.
	 * @param update    The condition if we have to update this container.
	 */
	default void singleUpdate(ItemContainer container, Item oldItem, Item newItem, int slot, boolean update) {
	}
	
	/**
	 * Fired when an {@link Item}s are added, removed, or replaced in bulk. This is to prevent firing multiple {@code
	 * singleUpdate(ItemContainer, int)} events for a single operation.
	 * @param container The {@link ItemContainer} firing the event.
	 */
	default void bulkUpdate(ItemContainer container) {
	}
	
	/**
	 * Fired when the capacity of {@code container} is exceeded.
	 * @param container The {@link ItemContainer} firing the event.
	 */
	default void capacityExceeded(ItemContainer container) {
	}
}