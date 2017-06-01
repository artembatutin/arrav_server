package net.edge.content.commands.impl;

import net.edge.world.World;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"ban"}, rights = {Rights.DEVELOPER, Rights.ADMINISTRATOR, Rights.MODERATOR}, syntax = "Use this command as ::ban username")
public final class BanningCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player ban = World.getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(ban != null && (ban.getRights().less(Rights.MODERATOR) || player.getRights().equals(Rights.DEVELOPER)) && ban != player) {
			player.message("Successfully banned " + ban.getFormatUsername() + ".");
			ban.setBanned(true);
			World.queueLogout(ban);
		}
	}
	
}
