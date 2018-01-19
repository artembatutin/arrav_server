package net.arrav.content.commands.impl;

import net.arrav.content.commands.Command;
import net.arrav.content.commands.CommandSignature;
import net.arrav.content.skill.construction.Construction;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.entity.actor.player.assets.Rights;

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
