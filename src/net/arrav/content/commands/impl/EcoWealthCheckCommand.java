package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.util.eco.EconomyController;
import net.arrav.util.eco.impl.WealthReport;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"ecowealth"}, rights = {Rights.ADMINISTRATOR}, syntax = "Reports in-game wealth, ::ecowealth")
public final class EcoWealthCheckCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		if(true)
			return;
		EconomyController.run(new WealthReport());
	}
	
}
