package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.content.market.MarketItem;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;
import net.edge.world.node.item.Item;

@CommandSignature(alias = {"marketadd"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as ::marketadd itemId")
public final class MarketAddCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int id = Integer.parseInt(cmd[1]);
		MarketItem item = MarketItem.get(id);
		if(item == null) {
			player.message("Nulled");
			return;
		}
		item.setSearchable(true);
		player.message("Added to the market.");
	}
	
}