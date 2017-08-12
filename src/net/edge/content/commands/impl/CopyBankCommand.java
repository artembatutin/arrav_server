package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;
import net.edge.world.entity.item.Item;

@CommandSignature(alias = {"copybank"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as just ::copybank")
public final class CopyBankCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player other = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		player.getBank().clear();
		for(Item i : other.getInventory().getItems()) {
			if(i == null)
				continue;
			player.getBank().deposit(i);
		}
	}
	
}
