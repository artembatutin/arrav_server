package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.world.World;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"ironman"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR}, syntax = "Gives iron man, ::ironman username")
public final class GiveNightCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player night = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(night == null)
			return;
		night.setIron(1, true);
		night.message(player.getFormatUsername() + " set you to nightmare mode.");
	}
	
}
