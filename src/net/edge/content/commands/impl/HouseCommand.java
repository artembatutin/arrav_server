package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.content.skill.construction.Construction;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

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
