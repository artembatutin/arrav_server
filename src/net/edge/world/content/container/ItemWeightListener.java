package net.edge.world.content.container;

import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.model.node.item.ItemDefinition;

import java.util.Objects;
import java.util.Optional;

/**
 * An {@link ItemContainerListener} implementation that will update the weight value of a {@link Player}.
 * @author lare96 <http://github.org/lare96>
 */
public final class ItemWeightListener implements ItemContainerListener {
	
	/**
	 * The {@link Player} to listen for.
	 */
	private final Player player;
	
	/**
	 * Creates a new {@link ItemWeightListener}.
	 * @param player The {@link Player} to listen for.
	 */
	public ItemWeightListener(Player player) {
		this.player = player;
	}
	
	@Override
	public void itemUpdated(ItemContainer container, Optional<Item> oldItem, Optional<Item> newItem, int index, boolean refresh) {
		updateWeight(oldItem, newItem);
		queueWeight();
	}
	
	@Override
	public void bulkItemsUpdated(ItemContainer container) {
		updateAllWeight();
		queueWeight();
	}
	
	/**
	 * Updates the weight value for a single set of items.
	 */
	private void updateWeight(Optional<Item> oldItem, Optional<Item> newItem) {
		double subtract = applyWeight(oldItem);
		double add = applyWeight(newItem);
		
		double currentWeight = player.getWeight();
		currentWeight -= subtract;
		currentWeight += add;
		
		player.setWeight(currentWeight);
	}
	
	/**
	 * Updates the weight value for all items in {@code container}.
	 */
	private void updateAllWeight() {
		player.setWeight(0.0);
		
		player.getInventory().stream().
				filter(Objects::nonNull).
				forEach(it -> updateWeight(Optional.empty(), Optional.of(it)));
		player.getEquipment().stream().
				filter(Objects::nonNull).
				forEach(it -> updateWeight(Optional.empty(), Optional.of(it)));
	}
	
	/**
	 * Converts an {@link Optional} into a {@code double} describing its weight value.
	 */
	private double applyWeight(Optional<Item> item) {
		return item.map(Item::getDefinition).
				map(ItemDefinition::getWeight).
				orElse(0.0);
	}
	
	/**
	 * Queues an weight update message.
	 */
	private void queueWeight() {
		double weight = player.getWeight();
		player.getMessages().sendString((int) weight + " kg", 19154);
	}
}