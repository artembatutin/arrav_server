package net.arrav.content.market.currency.impl;

import net.arrav.content.market.currency.GeneralCurrency;
import net.arrav.world.entity.actor.player.Player;

/**
 * The slayer currency based on the {@link Player#slayerPoints} value.
 * It is recommended that this be used rather than {@link GeneralCurrency}.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class SlayerCurrency implements GeneralCurrency {

	@Override
	public boolean takeCurrency(Player player, int amount) {
		if(player.getSlayerPoints() >= amount) {
			player.updateSlayers(-amount);
			return true;
		} else {
			player.message("You do not have enough slayer points");
			return false;
		}
	}

	@Override
	public void recieveCurrency(Player player, int amount) {
		player.updateSlayers(amount);
		player.message("You received " + amount + " slayer points!");
	}

	@Override
	public int currencyAmount(Player player) {
		return player.getSlayerPoints();
	}

	@Override
	public boolean canRecieveCurrency(Player player) {
		return true;
	}

	@Override
	public String toString() {
		return "points";
	}
}
