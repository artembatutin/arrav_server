package com.rageps.command.impl;

import com.rageps.world.World;
import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"copybank"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR}, syntax = "Copies a player bank, ::bank username")
public final class CopyBankCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player other = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		player.getBank().open(other);
	}
	
}
