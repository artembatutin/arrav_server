package net.edge.world.node.item.container;

import net.edge.world.node.entity.player.Player;
import net.edge.world.node.item.Item;

/**
 * An adapter for {@link ItemContainerListener} that updates {@link Item}s on a widget whenever items change, and sends the
 * underlying {@link Player} a message when the container is full.
 * @author lare96 <http://github.org/lare96>
 */
public abstract class ItemContainerAdapter implements ItemContainerListener {
	
	/**
	 * The {@link Player} instance.
	 */
	private final Player player;
	
	/**
	 * Creates a new {@link ItemContainerAdapter}.
	 * @param player The {@link Player} instance.
	 */
	public ItemContainerAdapter(Player player) {
		this.player = player;
	}
	
	@Override
	public void singleUpdate(ItemContainer container, Item oldItem, Item newItem, int slot, boolean update) {
		if(update)
			updateItem(newItem, slot);
	}
	
	@Override
	public void bulkUpdate(ItemContainer container) {
		updateItems(container);
	}
	
	@Override
	public void capacityExceeded(ItemContainer container) {
		player.message(getCapacityExceededMsg());
	}
	
	/**
	 * Updates many items on a widget.
	 */
	protected void updateItems(ItemContainer container) {
		player.getMessages().sendItemsOnInterface(widget(), container.getItems());
	}
	
	/**
	 * Updates a single item on a widget.
	 */
	protected void updateItem(Item item, int slot) {
		player.getMessages().sendItemOnInterfaceSlot(widget(), item, slot);
	}
	
	/**
	 * @return The id number of the widget this adapter is assigned to
	 */
	public abstract int widget();
	
	/**
	 * @return The message sent when the {@link ItemContainer} exceeds its capacity.
	 */
	public abstract String getCapacityExceededMsg();
}