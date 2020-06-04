package com.rageps.command.impl;

import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"setbankpin"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR}, syntax = "Sets the bank pin.")
public final class setBankPinCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.getBank().open(true);
	}
	
}
