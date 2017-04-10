package net.edge.world.content.commands.impl;

import net.edge.world.World;
import net.edge.world.content.commands.Command;
import net.edge.world.content.commands.CommandSignature;
import net.edge.world.model.node.entity.player.Player;
import net.edge.world.model.node.entity.player.assets.Rights;

@CommandSignature(alias = {"unmute"}, rights = {Rights.DEVELOPER, Rights.ADMINISTRATOR, Rights.MODERATOR}, syntax = "Use this command as ::unmute username")
public final class UnMutingCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player mute = World.getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(mute != null && (mute.getRights().less(Rights.MODERATOR) || player.getRights().equals(Rights.DEVELOPER)) && mute != player) {
			player.message("Successfully un-muted " + mute.getFormatUsername() + ".");
			mute.message("@red@You have been un-muted by " + player.getFormatUsername() + ".");
			mute.setMuted(false);
		}
	}
}
