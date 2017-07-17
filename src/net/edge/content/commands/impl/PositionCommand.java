package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"mypos"}, rights = {Rights.ADMINISTRATOR, Rights.ADMINISTRATOR, Rights.MODERATOR}, syntax = "Use this command as just ::mypos")
public final class PositionCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.message("You are at: " + player.getPosition().toString());
	}
	
}
