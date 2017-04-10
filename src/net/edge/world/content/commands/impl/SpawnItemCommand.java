package net.edge.world.content.commands.impl;

import net.edge.world.content.commands.CommandSignature;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.item.Item;
import net.edge.world.content.commands.Command;
import net.edge.world.model.node.entity.player.assets.Rights;

@CommandSignature(alias = {"pickup", "item", "spawn"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as ::pickup, ::item or ::spawn id [amount is optional]")
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
