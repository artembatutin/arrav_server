package com.rageps.content.commands.impl;

import com.rageps.content.commands.Command;
import com.rageps.content.commands.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"bank"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR}, syntax = "Opens the bank, ::bank")
public final class OpenBankCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.getBank().open(false);
	}
	
}
