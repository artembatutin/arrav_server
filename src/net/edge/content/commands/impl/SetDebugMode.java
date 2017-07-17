package net.edge.content.commands.impl;

import net.edge.GameServer;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"debug", "debugmode"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as just ::debug or ::debugmode")
public final class SetDebugMode implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		GameServer.DEBUG = !GameServer.DEBUG;
		player.message("Debug mode is set to: " + GameServer.DEBUG);
	}
	
}
