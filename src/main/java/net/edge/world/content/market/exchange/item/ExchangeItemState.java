package net.edge.world.content.market.exchange.item;

/**
 * Represents the two states of a {@link ExchangeItem}.
 */
public enum ExchangeItemState {
	
	/**
	 * The item can be bought anytime.
	 */
	GLOBAL,
	
	/**
	 * The item is in someone's shop.
	 */
	OFFER,
	
	/**
	 * The item is desired by a player.
	 */
	DEMAND
	
}
