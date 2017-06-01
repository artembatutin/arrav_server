package net.edge.world.content.commands.impl;

import net.edge.world.World;
import net.edge.world.content.commands.Command;
import net.edge.world.content.commands.CommandSignature;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"pinstance"}, rights = {Rights.DEVELOPER}, syntax = "Use this command as ::pinstance player #")
public final class SetPlayerInstanceCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player p = World.getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(p == null)
			return;
		int instance = Integer.parseInt(cmd[2]);
		p.setInstance(instance);
	}
	
}
