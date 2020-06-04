package com.rageps.command.impl;

import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"up", "down"}, rights = {Rights.ADMINISTRATOR, Rights.MODERATOR}, syntax = "Moves your height, ::up")
public final class HeightLevelCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		if(command.equalsIgnoreCase("up")) {
			player.move(player.getPosition().copy().move(0, 0, 1));
		} else {
			if(player.getPosition().getZ() != 0)
			player.move(player.getPosition().copy().move(0, 0, -1));
			else
				player.message("You are on the lowest height level.");
		}
	}
	
}
