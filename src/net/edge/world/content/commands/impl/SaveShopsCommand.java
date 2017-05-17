package net.edge.world.content.commands.impl;

import net.edge.world.content.commands.Command;
import net.edge.world.content.commands.CommandSignature;
import net.edge.world.content.market.MarketCounter;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.player.assets.Rights;

@CommandSignature(alias = {"saveshops"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as ::saveshops")
public final class SaveShopsCommand implements Command {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		MarketCounter.serializeShops();
	}

}
