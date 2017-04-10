package net.edge.world.content.container;

import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;

import java.util.Optional;

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
	public void itemUpdated(ItemContainer container, Optional<Item> oldItem, Optional<Item> newItem, int index, boolean refresh) {
		if(refresh)
			sendItemsToWidget(container);
	}
	
	@Override
	public void bulkItemsUpdated(ItemContainer container) {
		sendItemsToWidget(container);
	}
	
	@Override
	public void capacityExceeded(ItemContainer container) {
		player.message(getCapacityExceededMsg());
	}
	
	/**
	 * Queues a message that displays items from an {@link ItemContainer} on a widget.
	 */
	protected void sendItemsToWidget(ItemContainer container) {
		container.refresh(player, getWidgetId());
	}
	
	/**
	 * @return The widget to display items on.
	 */
	public abstract int getWidgetId();
	
	/**
	 * @return The message sent when the {@link ItemContainer} exceeds its capacity.
	 */
	public abstract String getCapacityExceededMsg();
}