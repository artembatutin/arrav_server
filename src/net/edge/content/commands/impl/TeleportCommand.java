package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"tele", "teleport"}, rights = {Rights.ADMINISTRATOR, Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR}, syntax = "Teleports to a coordinate, ::tele x y z")
public final class TeleportCommand implements Command {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int x = Integer.parseInt(cmd[1]);
		int y = Integer.parseInt(cmd[2]);
		int z = player.getPosition().getZ();
		if(cmd.length > 3) {
			z = Integer.parseInt(cmd[3]);
		}
		//		DefaultTeleportSpell.startTeleport(player, new Position(x, y, z)); TODO: add teleports
	}

}
