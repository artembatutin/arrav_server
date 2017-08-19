package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"mute"}, rights = {Rights.ADMINISTRATOR, Rights.ADMINISTRATOR, Rights.MODERATOR, Rights.HELPER}, syntax = "Mutes a player, ::mute username")
public final class MutingCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player muted = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(muted != null && (muted.getRights().less(Rights.MODERATOR) || player.getRights().equals(Rights.ADMINISTRATOR)) && muted != player) {
			player.message("Successfully muted " + muted.getFormatUsername() + ".");
			muted.message("@red@You have been muted by " + player.getFormatUsername() + ".");
			muted.setMuted(true);
		}
	}
}
