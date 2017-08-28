package net.edge.util.log.impl;

import net.edge.util.log.LogDetails;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.container.ItemContainer;

import java.util.Optional;

/**
 * The class which represents a trade log.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class ContainerLog extends LogDetails {

	/**
	 * The store title.
	 */
	private final ItemContainer container;

	private final Item oldItem, newItem;

	/**
	 * The item that the user bought.
	 */
	private final int slot;

	/**
	 * Constructs a new {@link ContainerLog}.
	 */
	public ContainerLog(Player player, ItemContainer container, Item oldItem, Item newItem, int slot) {
		super(player.getFormatUsername(), "Container");
		this.container = container;
		this.oldItem = oldItem;
		this.newItem = newItem;
		this.slot = slot;
	}

	@Override
	public Optional<String> formatInformation() {
		StringBuilder builder = new StringBuilder();
		builder.append("[\n");
		builder.append("   [Con: " + container.getClass().getSimpleName() + ", Old: " + oldItem + ", New: " + newItem + ", Slot: " + slot + ",]\n");
		return Optional.of(builder.toString());
	}
}
