package net.arrav.content.market.currency;

import net.arrav.world.entity.actor.player.Player;

/**
 * The parent class of all currencies that provides basic functionality for any
 * general currency. This can be used to register tangible, and even intangible
 * currencies.
 * @author lare96 <http://github.com/lare96>
 */
public interface GeneralCurrency {

	/**
	 * The method executed when the currency is taken from {@code player}.
	 * @param player the player the currency is taken from.
	 * @param amount the amount of currency that is taken.
	 */
	boolean takeCurrency(Player player, int amount);

	/**
	 * The method executed when the currency is given to {@code player}.
	 * @param player the player the currency is given to.
	 * @param amount the amount of currency that is given.
	 */
	void recieveCurrency(Player player, int amount);

	/**
	 * The method that retrieves the amount of currency {@code player} currently
	 * has.
	 * @param player the player who's currency amount will be determined.
	 * @return the amount of the currency the player has.
	 */
	int currencyAmount(Player player);

	/**
	 * Determines if the currency can be received when {@code player}'s
	 * inventory is full.
	 * @param player the player to determine this for.
	 * @return {@code true} if the currency can be recieved, {@code false}
	 * otherwise.
	 */
	boolean canRecieveCurrency(Player player);

	/**
	 * Determines if this currency is tangible.
	 * @return {@code true} if it is, {@code false} otherwise.
	 */
	default boolean tangible() {
		return false;
	}

	/**
	 * Gets the name of the currency.
	 */
	String toString();
}
