package com.rageps.util.log.impl;

import com.rageps.util.log.LogDetails;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;

import java.util.Optional;

/**
 * The class which represents a trade log.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class ShopLog extends LogDetails {
	
	/**
	 * The store title.
	 */
	private final String store;
	
	/**
	 * The item that the user sold.
	 */
	private final Item sold;
	
	/**
	 * The item that the user bought.
	 */
	private final Item bought;
	
	/**
	 * Constructs a new {@link ShopLog}.
	 * @param player {@link #getUsername()}.
	 * @param sold {@link #sold}.
	 * @param bought {@link #bought}.
	 */
	public ShopLog(Player player, Item sold, Item bought) {
		super(player.getFormatUsername(), "Shop");
		this.store = player.getMarketShop().getTitle();
		this.sold = sold;
		this.bought = bought;
	}
	
	@Override
	public Optional<String> formatInformation() {
		StringBuilder builder = new StringBuilder();
		builder.append("[\n");
		if(sold != null) {
			builder.append("    Sold: [Amount: " + sold.getAmount() + ", Name: " + sold.getDefinition().getName() + ", Id = " + sold.getId() + "]\n");
		}
		if(bought != null) {
			builder.append("    Bought: [Amount: " + bought.getAmount() + ", Name: " + bought.getDefinition().getName() + ", Id = " + bought.getId() + "].\n");
		}
		builder.append("    Bough from store: " + store + ".\n");
		return Optional.of(builder.toString());
	}
}
