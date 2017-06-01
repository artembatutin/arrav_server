package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"interface"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as ::interface interfaceId")
public final class OpenInterfaceCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.getMessages().sendInterface(Integer.parseInt(cmd[1]));
	}
	
}
