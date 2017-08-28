package net.edge.content.commands.impl;

import net.edge.Application;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"debug", "debugmode"}, rights = {Rights.ADMINISTRATOR}, syntax = "Toggles debug mode, ::debug")
public final class SetDebugMode implements Command {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Application.DEBUG = !Application.DEBUG;
		player.message("Debug mode is set to: " + Application.DEBUG);
	}

}
