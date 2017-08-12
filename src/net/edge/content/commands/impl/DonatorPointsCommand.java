package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"donatorpoints"}, rights = {Rights.ADMINISTRATOR}, syntax = "Use this command as ::donatorpoints playername amount")
public final class DonatorPointsCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player p = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(p == null)
			return;
		int amount = Integer.parseInt(cmd[2]);
		p.increaseTotalDonated(amount);
		player.message("You've increased " + amount + "x for " + p.getFormatUsername() + ". Total points = " + p.getTotalDonated(false));
		p.message("You have been given " + amount + "x total donate tokens.");
	}
	
}
