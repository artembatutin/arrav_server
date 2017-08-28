package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.util.eco.EconomyController;
import net.edge.util.eco.impl.ItemEconomyWipe;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"ecowipe"}, rights = {Rights.ADMINISTRATOR}, syntax = "Reports wipes item from economy, ::ecowipe item")
public final class EcoItemWipeCommand implements Command {

	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int item = Integer.parseInt(cmd[1]);
		EconomyController.run(new ItemEconomyWipe(item));
	}

}
