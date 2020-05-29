package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.content.teleport.impl.DefaultTeleportSpell;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;
import net.arrav.world.locale.Position;

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
