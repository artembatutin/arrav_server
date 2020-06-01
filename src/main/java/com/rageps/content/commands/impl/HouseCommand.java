package com.rageps.content.commands.impl;

import com.rageps.content.commands.Command;
import com.rageps.content.commands.CommandSignature;
import com.rageps.content.skill.construction.Construction;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

/**
 * The assist command for staff members.
 */
@CommandSignature(alias = {"house"}, rights = {Rights.ADMINISTRATOR}, syntax = "House testing command, ::house")
public final class HouseCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Construction.buyHouse(player);
		Construction.enterHouse(player, true);
	}
	
}
