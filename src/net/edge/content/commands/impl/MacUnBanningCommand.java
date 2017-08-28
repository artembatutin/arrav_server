package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.net.host.HostListType;
import net.edge.net.host.HostManager;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

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
