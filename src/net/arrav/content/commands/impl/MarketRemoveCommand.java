package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.content.market.MarketItem;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"marketremove"}, rights = {Rights.ADMINISTRATOR}, syntax = "Makes an item non-searchable in the market, ::marketadd itemId")
public final class MarketRemoveCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int id = Integer.parseInt(cmd[1]);
		MarketItem item = MarketItem.get(id);
		if(item == null) {
			player.message("Item is nulled");
			return;
		}
		item.setSearchable(false);
		player.message("Removed from the market.");
	}
	
}
