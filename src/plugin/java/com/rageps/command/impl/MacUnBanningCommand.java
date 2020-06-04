package com.rageps.command.impl;

import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.net.host.HostListType;
import com.rageps.net.host.HostManager;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"macunban"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR}, syntax = "Mac unban, ::macunban username")
public final class MacUnBanningCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		if(HostManager.remove(cmd[1], HostListType.BANNED_MAC)) {
			player.message("Successfully Mac unbanned " + cmd[1] + ".");
		} else {
			player.message("Couldn't find punished user.");
		}
	}
	
}
