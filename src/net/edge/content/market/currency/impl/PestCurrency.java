package net.edge.content.market.currency.impl;

import net.edge.content.market.currency.GeneralCurrency;
import net.edge.world.node.entity.player.Player;

/**
 * The slayer currency based on the {@link Player#slayerPoints} value.
 * It is recommended that this be used rather than {@link GeneralCurrency}.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class PestCurrency implements GeneralCurrency {

	@Override
	public boolean takeCurrency(Player player, int amount) {
		if(player.getPest() >= amount) {
			player.updatePest(-amount);
			player.getMessages().sendString(player.getPest() + " points", 37007);
			return true;
		} else {
			player.message("You do not have enough pest points");
			return false;
		}
	}

	@Override
	public void recieveCurrency(Player player, int amount) {
		player.updatePest(amount);
		player.message("You received " + amount + " pest points!");
		player.getMessages().sendString(player.getPest() + " points", 37007);
	}

	@Override
	public int currencyAmount(Player player) {
		return player.getPest();
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
