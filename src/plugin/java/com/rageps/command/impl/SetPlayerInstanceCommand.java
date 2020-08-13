package com.rageps.command.impl;

import com.rageps.world.World;
import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"pinstance"}, rights = {Rights.ADMINISTRATOR}, syntax = "Set a player to an instance, ::pinstance player id")
public final class SetPlayerInstanceCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player p = World.get().getWorldUtil().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(p == null)
			return;
		int instance = Integer.parseInt(cmd[2]);
		p.setInstance(instance);
	}
	
}
