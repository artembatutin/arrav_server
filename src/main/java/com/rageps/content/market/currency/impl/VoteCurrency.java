package com.rageps.content.market.currency.impl;

import com.rageps.content.PlayerPanel;
import com.rageps.content.market.currency.GeneralCurrency;
import com.rageps.world.entity.actor.player.Player;

/**
 * The voting currency based on the {@link Player#votePoints} value.
 * It is recommended that this be used rather than {@link GeneralCurrency}.
 * @author Artem Batutin
 */
public final class VoteCurrency implements GeneralCurrency {

	@Override
	public boolean takeCurrency(Player player, int amount) {
		player.votePoints -= amount;
		PlayerPanel.VOTE.refresh(player, "@or3@ - Vote points: @yel@" + player.votePoints + " points", true);
		return true;
	}

	@Override
	public void recieveCurrency(Player player, int amount) {
		player.votePoints += amount;
		player.message("You received " + amount + " voting points!");
		PlayerPanel.VOTE.refresh(player, "@or3@ - Vote points: @yel@" + player.votePoints + " points", true);
	}

	@Override
	public int currencyAmount(Player player) {
		return player.votePoints;
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
