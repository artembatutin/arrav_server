package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"instance"}, rights = {Rights.ADMINISTRATOR,}, syntax = "Go to a new instance, ::instance id")
public final class SetInstanceCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int instance = Integer.parseInt(cmd[1]);
		player.setInstance(instance);
	}
	
}
