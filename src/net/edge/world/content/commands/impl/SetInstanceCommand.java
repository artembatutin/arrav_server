package net.edge.world.content.commands.impl;

import net.edge.world.content.commands.Command;
import net.edge.world.content.commands.CommandSignature;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"isntance"}, rights = {Rights.DEVELOPER, Rights.ADMINISTRATOR}, syntax = "Use this command as ::isntance #")
public final class SetInstanceCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int instance = Integer.parseInt(cmd[2]);
		player.setInstance(instance);
	}
	
}
