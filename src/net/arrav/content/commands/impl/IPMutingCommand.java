package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.net.host.HostListType;
import net.arrav.net.host.HostManager;
import net.arrav.world.World;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"ipmute"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR}, syntax = "IP mute, ::ipmute username")
public final class IPMutingCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player muted = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(muted != null && muted != player) {
			player.message("Successfully IP muted " + muted.getFormatUsername() + ".");
			muted.message("@red@You have been IP-muted by " + player.getFormatUsername() + ".");
			HostManager.add(muted, HostListType.MUTED_IP);
			muted.ipMuted = true;
		} else {
			player.message("Can't find " + cmd[1].replaceAll("_", " ") + ".");
		}
	}
	
}
