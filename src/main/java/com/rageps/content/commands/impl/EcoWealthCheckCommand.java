package com.rageps.content.commands.impl;

import com.rageps.content.commands.Command;
import com.rageps.content.commands.CommandSignature;
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
