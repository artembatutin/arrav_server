package com.rageps.command.impl;

import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.util.eco.EconomyController;
import com.rageps.util.eco.impl.WealthReport;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"ecowealth"}, rights = {Rights.ADMINISTRATOR}, syntax = "Reports in-game wealth, ::ecowealth")
public final class EcoWealthCheckCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		if(true)
			return;
		EconomyController.run(new WealthReport());
	}
	
}
