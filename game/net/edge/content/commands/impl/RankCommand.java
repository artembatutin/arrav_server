package net.edge.content.commands.impl;

import net.edge.world.World;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"rank"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as ::rank playername rank")
public final class RankCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player p = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(p == null)
			return;
		String rank = cmd[2].toUpperCase();
		Rights rights = Rights.valueOf(rank);
		p.setRights(rights);
		player.message("You've successfully set " + p.getFormatUsername() + " to " + rights.name() + ".");
		p.message("You have been ranked by " + player.getFormatUsername() + " to " + rights.name() + ".");
	}
	
}
