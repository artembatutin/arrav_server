package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"emptyinventory"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR}, syntax = "Empties your inventory, ::emptyinventory")
public final class EmptyInventoryCommand implements Command {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.getInventory().clear();
	}

}
