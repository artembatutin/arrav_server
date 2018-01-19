package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.net.host.HostListType;
import net.arrav.net.host.HostManager;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"ipunban"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR}, syntax = "IP unban, ::ipunban username")
public final class IPUnBanningCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		if(HostManager.remove(cmd[1], HostListType.BANNED_IP)) {
			player.message("Successfully IP unbanned " + cmd[1] + ".");
		} else {
			player.message("Couldn't find punished user.");
		}
	}
	
}
