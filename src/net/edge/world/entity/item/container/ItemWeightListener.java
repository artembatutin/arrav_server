package net.edge.world.entity.item.container;

import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;

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
	public void singleUpdate(ItemContainer container, Item oldItem, Item newItem, int slot, boolean update) {
		updateWeight(oldItem, newItem);
		queueWeight();
	}
	
	@Override
	public void bulkUpdate(ItemContainer container) {
		updateAllWeight();
		queueWeight();
	}
	
	/**
	 * Updates the weight value for a single set of items.
	 */
	private void updateWeight(Item oldItem, Item newItem) {
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
		for(Item item : player.getInventory().getItems()) {
			if(item == null)
				continue;
			updateWeight(null, item);
		}
		for(Item item : player.getEquipment().getItems()) {
			if(item == null)
				continue;
			updateWeight(null, item);
		}
	}
	
	/**
	 * Converts an {@link Optional} into a {@code double} describing its weight value.
	 */
	private double applyWeight(Item item) {
		if(item == null)
			return 0.0;
		if(item.getDefinition() == null)
			return 0.0;
		return item.getDefinition().getWeight();
	}
	
	/**
	 * Queues an weight update message.
	 */
	private void queueWeight() {
		double weight = player.getWeight();
		player.text(19154, (int) weight + " kg");
	}
}