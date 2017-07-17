package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"interface"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as ::interface interfaceId")
public final class OpenInterfaceCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.widget(Integer.parseInt(cmd[1]));
	}
	
}
