package net.edge.content.market.exchange.personal;

import net.edge.content.market.exchange.item.ExchangeItem;
import net.edge.content.market.exchange.personal.impl.DemandSection;
import net.edge.world.node.actor.player.Player;

/**
 * A counter acting as a shop and hosted by a {@link Player}.
 */
public class PlayerCounter {
	
	/**
	 * The player hosting this counter.
	 */
	private final Player player;
	
	/**
	 * The demand section of this counter.
	 */
	private PlayerCounterSection demand = new DemandSection();
	
	/**
	 * The demand section of this counter.
	 */
	private PlayerCounterSection offer = new DemandSection();
	
	/**
	 * Creates a new {@link PlayerCounter}.
	 * @param player the player hosting the counter.
	 */
	public PlayerCounter(Player player) {
		this.player = player;
	}
	
	/**
	 * Updating an item.
	 * @param item item to be updated.
	 */
	public void update(ExchangeItem item) {
		switch(item.getState()) {
			case DEMAND:
				demand.update(player, item);
				break;
			case OFFER:
				offer.update(player, item);
				break;
			case GLOBAL:
				player.message("Can't add this item.");
				break;
		}
	}
	
	/**
	 * Removing an item
	 * @param item item to be removed.
	 */
	public void remove(ExchangeItem item) {
		switch(item.getState()) {
			case DEMAND:
				demand.remove(player, item);
				break;
			case OFFER:
				offer.remove(player, item);
				break;
			case GLOBAL:
				player.message("Can't remove this item.");
				break;
		}
	}
	
}
