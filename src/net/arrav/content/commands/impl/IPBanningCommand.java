package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.net.host.HostListType;
import net.arrav.net.host.HostManager;
import net.arrav.world.World;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"ipban"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR}, syntax = "IP ban, ::ipban username")
public final class IPBanningCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player banned = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(banned != null && banned != player) {
			player.message("Successfully IP banned " + banned.getFormatUsername() + ".");
			HostManager.add(banned, HostListType.BANNED_IP);
			World.get().queueLogout(banned);
		} else {
			player.message("Can't find " + cmd[1].replaceAll("_", " ") + ".");
		}
	}
	
}
