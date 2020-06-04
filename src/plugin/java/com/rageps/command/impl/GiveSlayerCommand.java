package com.rageps.command.impl;

import com.rageps.world.World;
import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"slayer"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR}, syntax = "Gives slayer points ::slayer username amount")
public final class GiveSlayerCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player slayer = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		int amount = Integer.parseInt(cmd[2]);
		slayer.updateSlayers(amount);
		player.message("You gave " + amount + " slayer points to " + slayer.getFormatUsername());
		slayer.message("You received " + amount + " slayer points from " + player.getFormatUsername());
	}
	
}
