package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.net.PunishmentHandler;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"ipunban"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR}, syntax = "IP unban, ::ipunban username")
public final class IPUnBanningCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		if(PunishmentHandler.removeIPBan(cmd[1])) {
			player.message("Successfully IP unbanned " + cmd[1] + ".");
		} else {
			player.message("Couldn't find punished user.");
		}
	}
	
}
