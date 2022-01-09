package com.rageps.command.impl;

import com.rageps.world.World;
import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"movetome"}, rights = {Rights.ADMINISTRATOR, Rights.MODERATOR}, syntax = "Moves a player to you, ::movetome username")
public final class MoveToMeCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player p = World.get().getWorldUtil().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(p == null && p.getRights().greater(Rights.MODERATOR))
			return;
		p.move(player.getPosition());
		p.message("You have been moved to " + player.getFormatUsername() + "'s position.");
	}
	
}
