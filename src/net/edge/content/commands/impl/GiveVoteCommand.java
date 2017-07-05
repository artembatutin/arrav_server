package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.World;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"givevote"}, rights = {Rights.ADMINISTRATOR, Rights.ADMINISTRATOR}, syntax = "Use this command as ::givevote player amount")
public final class GiveVoteCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player vote = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(vote == null)
			return;
		int amount = Integer.parseInt(cmd[2]);
		vote.setVotePoints(vote.getVotePoints() + amount);
		player.message("You gave " + amount + " vote points to " + vote.getFormatUsername());
		vote.message("You received " + amount + " vote points from " + player.getFormatUsername());
	}
	
}
