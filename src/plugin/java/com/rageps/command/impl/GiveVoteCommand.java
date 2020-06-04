package com.rageps.command.impl;

import com.rageps.world.World;
import com.rageps.content.PlayerPanel;
import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

@CommandSignature(alias = {"votes"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR}, syntax = "Use this command as ::votes player amount")
public final class GiveVoteCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		Player vote = World.get().getPlayer(cmd[1].replaceAll("_", " ")).orElse(null);
		if(vote == null)
			return;
		int amount = Integer.parseInt(cmd[2]);
		vote.votePoints += amount;
		PlayerPanel.VOTE.refresh(player, "@or3@ - Vote points: @yel@" + player.votePoints + " points", true);
		player.message("You gave " + amount + " vote points to " + vote.getFormatUsername());
		vote.message("You received " + amount + " vote points from " + player.getFormatUsername());
	}
	
}
