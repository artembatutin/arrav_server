package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.util.eco.EconomyController;
import net.edge.util.eco.impl.WealthReport;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"ecowealth"}, rights = {Rights.ADMINISTRATOR}, syntax = "Reports in-game wealth, ::ecowealth")
public final class EcoWealthCheckCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		if(true)
			return;
		EconomyController.run(new WealthReport());
	}
	
}
