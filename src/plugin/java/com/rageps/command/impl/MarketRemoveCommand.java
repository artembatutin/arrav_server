package com.rageps.command.impl;

import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.content.market.MarketItem;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

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
