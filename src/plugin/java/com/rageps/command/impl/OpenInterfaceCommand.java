package com.rageps.command.impl;

import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"interface", "widget"}, rights = {Rights.ADMINISTRATOR}, syntax = "Opens an interface, ::interface id")
public final class OpenInterfaceCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.widget(Integer.parseInt(cmd[1]));
	}
	
}
