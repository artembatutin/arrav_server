package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.util.json.impl.MarketValueLoader;
import net.edge.util.json.impl.ShopLoader;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"reloadmarket"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as just ::reloadmarket.")
public final class ReloadMarket implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		new MarketValueLoader().load();
	}
	
}
