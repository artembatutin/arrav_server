package net.edge.content.commands.impl;

import net.edge.world.World;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"movetome"}, rights = {Rights.DEVELOPER, Rights.ADMINISTRATOR, Rights.MODERATOR}, syntax = "Use this command as ::movetome username")
public final class MoveToMeCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player p = World.getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(p == null && p.getRights().greater(Rights.MODERATOR))
			return;
		p.move(player.getPosition());
		p.message("You have been moved to " + player.getFormatUsername() + "'s position.");
	}
	
}
