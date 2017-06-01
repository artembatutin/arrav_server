package net.edge.world.content.commands.impl;

import net.edge.world.content.commands.Command;
import net.edge.world.content.commands.CommandSignature;
import net.edge.world.content.teleport.impl.DefaultTeleportSpell;
import net.edge.world.locale.Position;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"tele", "teleport"}, rights = {Rights.DEVELOPER, Rights.ADMINISTRATOR, Rights.SUPER_MODERATOR, Rights.MODERATOR}, syntax = "Use this command as ::tele or ::teleport x y [[z/height axis is optional]")
public final class TeleportCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int x = Integer.parseInt(cmd[1]);
		int y = Integer.parseInt(cmd[2]);
		int z = player.getPosition().getZ();
		if(cmd.length > 3) {
			z = Integer.parseInt(cmd[3]);
		}
		DefaultTeleportSpell.startTeleport(player, new Position(x, y, z));
	}
	
}
