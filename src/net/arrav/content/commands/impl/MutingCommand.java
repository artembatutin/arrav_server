package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.world.World;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"mute"}, rights = {Rights.ADMINISTRATOR, Rights.MODERATOR, Rights.HELPER}, syntax = "Mutes a player, ::mute username")
public final class MutingCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player muted = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(muted != null && muted != player) {
			player.message("Successfully muted " + muted.getFormatUsername() + ".");
			muted.message("@red@You have been muted by " + player.getFormatUsername() + ".");
			muted.muted = true;
		} else {
			player.message("Can't find player.");
		}
	}
}
