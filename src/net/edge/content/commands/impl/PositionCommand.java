package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"mypos", "pos"}, rights = {Rights.ADMINISTRATOR, Rights.MODERATOR}, syntax = "Gives coordinates, ::pos")
public final class PositionCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.message("You are at: " + player.getPosition().toString());
	}
	
}
