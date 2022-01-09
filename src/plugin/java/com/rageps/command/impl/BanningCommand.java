package com.rageps.command.impl;

import com.rageps.world.World;
import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"ban"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR}, syntax = "Bans a player, use ::ban username")
public final class BanningCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player ban = World.get().getWorldUtil().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(ban != null && (ban.getRights().less(Rights.MODERATOR) || player.getRights().equals(Rights.ADMINISTRATOR)) && ban != player) {
			player.message("Successfully banned " + ban.getFormatUsername() + ".");
			ban.banned = true;
			World.get().getGameService().unregisterPlayer(ban);
		}
	}
	
}
