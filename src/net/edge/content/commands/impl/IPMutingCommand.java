package net.edge.content.commands.impl;

import net.edge.net.PunishmentHandler;
import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"ipmute"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR}, syntax = "IP mute, ::ipmute username")
public final class IPMutingCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player muted = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(muted != null && muted != player) {
			player.message("Successfully IP muted " + muted.getFormatUsername() + ".");
			muted.message("@red@You have been IP-muted by " + player.getFormatUsername() + ".");
			PunishmentHandler.addIPMute(muted);
		} else {
			player.message("Can't find " + cmd[1].replaceAll("_", " ") + ".");
		}
	}
	
}
