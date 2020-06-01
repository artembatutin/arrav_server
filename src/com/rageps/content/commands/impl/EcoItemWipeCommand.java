package com.rageps.content.commands.impl;

import com.rageps.content.commands.Command;
import com.rageps.content.commands.CommandSignature;
import com.rageps.util.eco.EconomyController;
import com.rageps.util.eco.impl.ItemEconomyWipe;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

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
