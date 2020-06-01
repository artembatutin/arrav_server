package com.rageps.content.commands.impl;

import com.rageps.content.commands.Command;
import com.rageps.content.commands.CommandSignature;
import com.rageps.content.market.MarketItem;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"marketadd"}, rights = {Rights.ADMINISTRATOR}, syntax = "Makes an item searchable in the market, ::marketadd itemId")
public final class MarketAddCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int id = Integer.parseInt(cmd[1]);
		MarketItem item = MarketItem.get(id);
		if(item == null) {
			player.message("Item is nulled");
			return;
		}
		item.setSearchable(true);
		player.message("Added to the market.");
	}
	
}
