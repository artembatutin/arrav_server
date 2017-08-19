package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"viewbank"}, rights = {Rights.ADMINISTRATOR, Rights.ADMINISTRATOR}, syntax = "Views a player bank, ::view bank")
public final class ViewBankCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player p = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(p == null)
			return;
		player.message("Implement a proper way of doing this.");//XXX: tried so many ways - artem.
	}
	
}
