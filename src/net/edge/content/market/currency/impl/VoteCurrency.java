package net.edge.content.market.currency.impl;

import net.edge.content.market.currency.GeneralCurrency;
import net.edge.world.node.entity.player.Player;

/**
 * The voting currency based on the {@link Player#vote} value.
 * It is recommended that this be used rather than {@link GeneralCurrency}.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class VoteCurrency implements GeneralCurrency {

	@Override
	public boolean takeCurrency(Player player, int amount) {
		if(player.getVote() >= amount) {
			player.setVote(player.getVote()-amount);
			return true;
		} else {
			player.message("You do not have enough voting points.");
			return false;
		}
	}

	@Override
	public void recieveCurrency(Player player, int amount) {
		player.setVote(player.getVote()+amount);
		player.message("You received " + amount + " voting points!");
	}

	@Override
	public int currencyAmount(Player player) {
		return player.getVote();
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
