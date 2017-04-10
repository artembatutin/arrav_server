package net.edge.world.content.commands.impl;

import net.edge.world.content.commands.Command;
import net.edge.world.content.commands.CommandDispatcher;
import net.edge.world.content.commands.CommandSignature;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.player.assets.Rights;

@CommandSignature(alias = {"reloadcommands"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as just ::reloadcommands.")
public final class ReloadCommands implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		CommandDispatcher.reload();
		player.message("Successfully reloaded the commands on the world...");
	}
	
}
