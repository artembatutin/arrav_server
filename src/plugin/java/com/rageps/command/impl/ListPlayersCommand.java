package com.rageps.command.impl;

import com.rageps.world.World;
import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"listplayers"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR}, syntax = "Lists online players, ::listplayers")
public final class ListPlayersCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.message("There is currently " + World.get().getPlayers().size() + " player online.");
		if(!World.get().getPlayers().isEmpty()) {
			for(Player other : World.get().getPlayers()) {
				if(other == null)
					continue;
				player.message(" - " + other.getFormatUsername());
			}
		}
	}
	
}
