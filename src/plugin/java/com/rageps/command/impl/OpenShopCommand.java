package com.rageps.command.impl;

import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.content.market.MarketCounter;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"shop"}, rights = {Rights.ADMINISTRATOR}, syntax = "Opens a shop, ::shop id")
public final class OpenShopCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int id = Integer.parseInt(cmd[1]);
		if(MarketCounter.getShops().containsKey(id))
			MarketCounter.getShops().get(id).openShop(player);
		else
			player.message("Shop doesn't exist.");
	}
	
}
