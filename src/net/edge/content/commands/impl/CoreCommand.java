package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"core"}, rights = {Rights.ADMINISTRATOR}, syntax = "Sends core information about the core.")
public final class CoreCommand implements Command {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.message("Took " + World.millis + "ms on sync.");
	}

}
