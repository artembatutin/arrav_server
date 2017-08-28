package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;
import net.edge.world.entity.item.Item;

@CommandSignature(alias = {"copyinventory"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR}, syntax = "Copies player's inventory, ::copyinventory username")
public final class CopyInventoryCommand implements Command {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player other = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		player.getInventory().clear();
		for(Item i : other.getInventory().getItems()) {
			if(i == null)
				continue;
			player.getInventory().add(i);
		}
	}

}
