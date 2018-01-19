package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.util.eco.EconomyController;
import net.arrav.util.eco.impl.ItemEconomyWipe;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"ecowipe"}, rights = {Rights.ADMINISTRATOR}, syntax = "Reports wipes item from economy, ::ecowipe item")
public final class EcoItemWipeCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		if(true)
			return;
		int item = Integer.parseInt(cmd[1]);
		EconomyController.run(new ItemEconomyWipe(item));
	}
	
}
