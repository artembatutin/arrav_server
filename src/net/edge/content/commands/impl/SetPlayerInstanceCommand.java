package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"pinstance"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as ::pinstance player #")
public final class SetPlayerInstanceCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player p = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(p == null)
			return;
		int instance = Integer.parseInt(cmd[2]);
		p.setInstance(instance);
	}
	
}
