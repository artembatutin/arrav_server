package com.rageps.content.commands.impl;

import com.rageps.Arrav;
import com.rageps.content.commands.Command;
import com.rageps.content.commands.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"debug", "debugmode"}, rights = {Rights.ADMINISTRATOR}, syntax = "Toggles debug mode, ::debug")
public final class SetDebugMode implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Arrav.DEBUG = !Arrav.DEBUG;
		player.message("Debug mode is set to: " + Arrav.DEBUG);
	}
	
}
