package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.net.host.HostListType;
import net.arrav.net.host.HostManager;
import net.arrav.world.World;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"macban"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR}, syntax = "mac ban, ::macban username")
public final class MacBanningCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player banned = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(banned != null && banned.same(player)) {
			player.message("Successfully Mac banned " + banned.getFormatUsername() + ".");
			if(HostManager.add(banned, HostListType.BANNED_MAC))
				World.get().queueLogout(banned);
			else
				player.message("User has an invalid mac address, try something else.");
		} else {
			player.message("Can't find " + cmd[1].replaceAll("_", " ") + ".");
		}
	}
	
}
