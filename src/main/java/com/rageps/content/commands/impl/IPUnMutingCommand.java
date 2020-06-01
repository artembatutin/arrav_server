package com.rageps.content.commands.impl;

import com.rageps.world.World;
import com.rageps.content.commands.Command;
import com.rageps.content.commands.CommandSignature;
import com.rageps.net.host.HostListType;
import com.rageps.net.host.HostManager;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

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
