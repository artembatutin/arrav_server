package com.rageps.command.impl;

import com.rageps.world.World;
import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"moveto"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR}, syntax = "Moves to a player, ::moveto username")
public final class MoveToCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player p = World.get().getWorldUtil().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(p == null) {
			player.message("Cant find " + cmd[1].replaceAll("_", " ") + ".");
			return;
		}
		player.move(p.getPosition());
		player.message("You moved to " + p.getFormatUsername() + "'s position.");
	}
	
}
