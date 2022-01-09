package com.rageps.command.impl;

import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.item.Item;

@CommandSignature(alias = {"pickup", "item", "spawn"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR, Rights.GOLDEN_DONATOR, Rights.EXTREME_DONATOR, Rights.SUPER_DONATOR, Rights.DONATOR, Rights.IRON_MAN, Rights.DESIGNER, Rights.YOUTUBER, Rights.HELPER, Rights.PLAYER}, syntax = "Spawns a new item, ::item id am")
public final class SpawnItemCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int id = Integer.parseInt(cmd[1]);
		int amount = 1;
		if(cmd.length > 2) {
			amount = Integer.parseInt(cmd[2]);
		}
		player.getInventory().add(new Item(id, amount));
	}
	
}
