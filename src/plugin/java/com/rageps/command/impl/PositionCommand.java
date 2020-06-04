package com.rageps.command.impl;

import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"mypos", "pos"}, rights = {Rights.ADMINISTRATOR, Rights.MODERATOR}, syntax = "Gives coordinates, ::pos")
public final class PositionCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.message("You are at: " + player.getPosition().toString());
	}
	
}
