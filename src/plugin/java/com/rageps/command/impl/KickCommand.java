package com.rageps.command.impl;

import com.rageps.world.World;
import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"kick"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR, Rights.HELPER}, syntax = "Kick a player, ::kick username")
public final class KickCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player kick = World.get().getWorldUtil().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(kick != null && kick != player) {
			player.message("Successfully kicked " + kick.getFormatUsername() + ".");
			World.get().queueLogout(kick);
		} else {
			player.message("Can't find " + cmd[1].replaceAll("_", " ") + ".");
		}
	}
	
}
