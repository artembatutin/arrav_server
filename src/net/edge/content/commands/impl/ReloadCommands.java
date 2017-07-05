package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandDispatcher;
import net.edge.content.commands.CommandSignature;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"reloadcommands"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as just ::reloadcommands.")
public final class ReloadCommands implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		CommandDispatcher.reload();
		player.message("Successfully reloaded the commands on the world...");
	}
	
}
