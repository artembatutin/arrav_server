package net.edge.content.market.currency.impl;

import net.edge.content.market.currency.GeneralCurrency;
import net.edge.world.node.actor.player.Player;

/**
 * The voting currency based on the {@link Player#votePoints} value.
 * It is recommended that this be used rather than {@link GeneralCurrency}.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class VoteCurrency implements GeneralCurrency {

	@Override
	public boolean takeCurrency(Player player, int amount) {
		player.setVotePoints(-amount);
		return true;
	}

	@Override
	public void recieveCurrency(Player player, int amount) {
		player.setVotePoints(amount);
		player.message("You received " + amount + " voting points!");
	}

	@Override
	public int currencyAmount(Player player) {
		return player.getVotePoints();
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
