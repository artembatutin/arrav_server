package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

import java.util.Iterator;

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
