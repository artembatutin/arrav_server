package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.content.market.MarketCounter;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"saveshops"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as ::saveshops")
public final class SaveShopsCommand implements Command {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		MarketCounter.serializeShops();
	}

}
