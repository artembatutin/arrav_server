package com.rageps.world.entity.item.container;

import com.rageps.net.refactor.packet.out.model.ItemOnInterfaceSlotPacket;
import com.rageps.net.refactor.packet.out.model.ItemsOnInterfacePacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.net.packet.out.SendContainer;
import com.rageps.net.packet.out.SendItemOnInterfaceSlot;
import com.rageps.world.entity.item.Item;

/**
 * An adapter for {@link ItemContainerListener} that updates {@link Item}s on a
 * widget whenever items change, and sends the underlying {@link Player} a
 * message when the container is full.
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
			updateItem(container, newItem, slot);
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
		player.send(new ItemsOnInterfacePacket(player, widget(), container));
	}
	
	/**
	 * Updates a single item on a widget.
	 */
	protected void updateItem(ItemContainer container, Item item, int slot) {
		player.send(new ItemOnInterfaceSlotPacket(widget(), item, slot));
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