package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"vote", "voting"}, rights = {Rights.ADMINISTRATOR, Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR, Rights.GOLDEN_DONATOR, Rights.EXTREME_DONATOR, Rights.SUPER_DONATOR, Rights.DONATOR, Rights.IRON_MAN, Rights.DESIGNER, Rights.YOUTUBER, Rights.HELPER, Rights.PLAYER}, syntax = "Use this command as ::vote")
public final class VoteCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.getMessages().sendLink("vote");
		player.message("Do ::redeem once you're done. Thank you for voting!");
	}
	
}
