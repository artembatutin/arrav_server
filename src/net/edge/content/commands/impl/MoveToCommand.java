package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"moveto"}, rights = {Rights.ADMINISTRATOR, Rights.ADMINISTRATOR, Rights.MODERATOR}, syntax = "Use this command as ::moveto username")
public final class MoveToCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player p = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(p == null) {
			player.message("Cant find " + cmd[1].replaceAll("_", " ") + ".");
			return;
		}
		player.move(p.getPosition());
		player.message("You moved to " + p.getFormatUsername() + "'s position.");
	}
	
}
