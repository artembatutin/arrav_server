package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.net.host.HostListType;
import net.edge.net.host.HostManager;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

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
