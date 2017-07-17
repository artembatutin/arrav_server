package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.node.actor.player.Player;
import net.edge.world.node.actor.player.assets.Rights;
import net.edge.world.node.item.Item;

@CommandSignature(alias = {"pickup", "item", "spawn"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as ::pickup, ::item or ::spawn id [amount is optional]")
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
