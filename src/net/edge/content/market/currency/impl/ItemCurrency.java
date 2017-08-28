package net.edge.content.market.currency.impl;

import net.edge.content.market.currency.GeneralCurrency;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.item.Item;
import net.edge.world.entity.item.ItemDefinition;

import java.util.Optional;

/**
 * The currency that provides basic functionality for all tangible currencies.
 * It is recommended that this be used rather than {@link GeneralCurrency}.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemCurrency implements GeneralCurrency {

	/**
	 * The item identification for this currency.
	 */
	private final int id;

	/**
	 * An optional format string that should be displayed on the shop interface.
	 */
	private final Optional<String> format;

	/**
	 * Creates a new {@link ItemCurrency}.
	 *
	 * @param id     {@link #id}.
	 * @param format {@link #format}.
	 */
	public ItemCurrency(int id, Optional<String> format) {
		this.id = id;
		this.format = format;
	}

	/**
	 * Creates a new {@link ItemCurrency}.
	 *
	 * @param id the item identification for this currency.
	 */
	public ItemCurrency(int id) {
		this(id, Optional.empty());
	}

	@Override
	public boolean takeCurrency(Player player, int amount) {
		if(player.getInventory().canRemove(new Item(id, amount))) {
			player.getInventory().remove(new Item(id, amount));
			return true;
		}
		return false;
	}

	@Override
	public void recieveCurrency(Player player, int amount) {
		player.getInventory().add(new Item(id, amount));
	}

	@Override
	public int currencyAmount(Player player) {
		return player.getInventory().computeAmountForId(id);
	}

	@Override
	public boolean canRecieveCurrency(Player player) {
		return player.getInventory().contains(id);
	}

	@Override
	public boolean tangible() {
		return true;
	}

	@Override
	public String toString() {
		return format.orElse(ItemDefinition.DEFINITIONS[id].getName());
	}

	/**
	 * Gets the item identification for this currency.
	 *
	 * @return the item identification.
	 */
	public int getId() {
		return id;
	}
}
