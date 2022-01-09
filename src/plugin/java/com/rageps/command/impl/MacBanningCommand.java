package com.rageps.command.impl;

import com.rageps.world.World;
import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.net.host.HostListType;
import com.rageps.net.host.HostManager;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"macban"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR}, syntax = "mac ban, ::macban username")
public final class MacBanningCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player banned = World.get().getWorldUtil().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(banned != null && banned.same(player)) {
			player.message("Successfully Mac banned " + banned.getFormatUsername() + ".");
			if(HostManager.add(banned, HostListType.BANNED_MAC))
			World.get().getGameService().unregisterPlayer(banned);
			else
				player.message("User has an invalid mac address, try something else.");
		} else {
			player.message("Can't find " + cmd[1].replaceAll("_", " ") + ".");
		}
	}
	
}
