package net.edge.content.commands.impl;

import net.edge.world.World;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"viewbank"}, rights = {Rights.DEVELOPER, Rights.ADMINISTRATOR}, syntax = "Use this command as ::viewbank username")
public final class ViewBankCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player p = World.getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(p == null)
			return;
		player.message("Implement a proper way of doing this.");//XXX: tried so many ways - artem.
	}
	
}
