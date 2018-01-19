package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.net.host.HostListType;
import net.arrav.net.host.HostManager;
import net.arrav.world.World;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"ipunmute"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR}, syntax = "IP unmute, ::ipunmute username")
public final class IPUnMutingCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player muted = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(HostManager.remove(cmd[1], HostListType.MUTED_IP)) {
			player.message("Successfully IP unmuted " + cmd[1] + ".");
			if(muted != null) {
				muted.ipMuted = false;
				muted.message("You have been IP unmuted by " + player.getFormatUsername() + ".");
			}
		} else {
			player.message("Couldn't find punished user.");
		}
	}
	
}
