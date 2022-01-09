package com.rageps.command.impl;

import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.locale.Position;

@CommandSignature(alias = {"tele", "teleport"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR}, syntax = "Teleports to a coordinate, ::tele x y z")
public final class TeleportCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int x = Integer.parseInt(cmd[1]);
		int y = Integer.parseInt(cmd[2]);
		int z = player.getPosition().getZ();
		if(cmd.length > 3) {
			z = Integer.parseInt(cmd[3]);
		}
		player.move(new Position(x, y, z));
	}
	
}
