package com.rageps.content.commands.impl;

import com.rageps.world.World;
import com.rageps.content.commands.Command;
import com.rageps.content.commands.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"kick"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR, Rights.HELPER}, syntax = "Kick a player, ::kick username")
public final class KickCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player kick = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(kick != null && kick != player) {
			player.message("Successfully kicked " + kick.getFormatUsername() + ".");
			World.get().queueLogout(kick);
		} else {
			player.message("Can't find " + cmd[1].replaceAll("_", " ") + ".");
		}
	}
	
}
