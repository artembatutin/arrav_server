package com.rageps.command.impl;

import com.rageps.world.World;
import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"core"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR}, syntax = "Sends core information about the core.")
public final class CoreCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.message("Took " + World.millis + "ms on sync");
	}
	
}
