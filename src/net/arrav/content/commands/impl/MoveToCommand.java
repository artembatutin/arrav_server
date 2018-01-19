package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.world.World;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"moveto"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR}, syntax = "Moves to a player, ::moveto username")
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
