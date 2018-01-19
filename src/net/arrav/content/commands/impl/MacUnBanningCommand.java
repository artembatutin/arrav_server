package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.net.host.HostListType;
import net.arrav.net.host.HostManager;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

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
