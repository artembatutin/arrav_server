package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;
import net.edge.world.entity.item.Item;

@CommandSignature(alias = {"pickup", "item", "spawn"}, rights = {Rights.ADMINISTRATOR}, syntax = "Spawns a new item, ::item id am")
public final class SpawnItemCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int id = Integer.parseInt(cmd[1]);
		int amount = 1;
		if(cmd.length > 2) {
			amount = Integer.parseInt(cmd[2]);
		}
		player.getInventory().add(new Item(id, amount));
	}
	
}
