package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.util.json.impl.MobDropTableLoader;
import net.edge.util.json.impl.ShopLoader;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"reloaddrops"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as just ::reloaddrops.")
public final class ReloadDrops implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		new MobDropTableLoader().load();
	}
	
}
