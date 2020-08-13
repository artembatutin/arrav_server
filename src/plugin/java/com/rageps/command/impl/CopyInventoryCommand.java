package com.rageps.command.impl;

import com.rageps.world.World;
import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;
import com.rageps.world.entity.item.Item;

@CommandSignature(alias = {"copyinventory"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR}, syntax = "Copies player's inventory, ::copyinventory username")
public final class CopyInventoryCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player other = World.get().getWorldUtil().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		player.getInventory().clear();
		for(Item i : other.getInventory().getItems()) {
			if(i == null)
				continue;
			player.getInventory().add(i);
		}
	}
	
}
