package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandDispatcher;
import net.edge.content.commands.CommandSignature;
import net.edge.util.json.impl.ShopLoader;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"reloadshop"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as just ::reloadshop.")
public final class ReloadShops implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		new ShopLoader().load();
	}
	
}
