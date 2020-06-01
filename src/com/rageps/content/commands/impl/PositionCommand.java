package com.rageps.content.commands.impl;

import com.rageps.content.commands.Command;
import com.rageps.content.commands.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"mypos", "pos"}, rights = {Rights.ADMINISTRATOR, Rights.MODERATOR}, syntax = "Gives coordinates, ::pos")
public final class PositionCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.message("You are at: " + player.getPosition().toString());
	}
	
}
