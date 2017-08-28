package net.edge.world.entity.item.container;

import net.edge.net.packet.out.SendContainer;
import net.edge.net.packet.out.SendItemOnInterfaceSlot;
import net.edge.util.log.Log;
import net.edge.util.log.impl.ContainerLog;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

/**
 * An adapter for {@link ItemContainerListener} that updates {@link Item}s on a
 * widget whenever items change, and sends the underlying {@link Player} a
 * message when the container is full.
 *
 * @author lare96 <http://github.org/lare96>
 */
public abstract class ItemContainerAdapter implements ItemContainerListener {

	/**
	 * The {@link Player} instance.
	 */
	private final Player player;

	/**
	 * Creates a new {@link ItemContainerAdapter}.
	 *
	 * @param player The {@link Player} instance.
	 */
	public ItemContainerAdapter(Player player) {
		this.player = player;
	}

	@Override
	public void singleUpdate(ItemContainer container, Item oldItem, Item newItem, int slot, boolean update) {
		if(update)
			updateItem(container, newItem, slot);
		World.getLoggingManager().write(Log.create(new ContainerLog(player, container, oldItem, newItem, slot)));
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
		player.out(new SendContainer(widget(), container));
	}

	/**
	 * Updates a single item on a widget.
	 */
	protected void updateItem(ItemContainer container, Item item, int slot) {
		player.out(new SendItemOnInterfaceSlot(widget(), item, slot));
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