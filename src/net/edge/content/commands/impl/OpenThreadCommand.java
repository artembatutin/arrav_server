package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.node.entity.player.Player;
import net.edge.world.node.entity.player.assets.Rights;

@CommandSignature(alias = {"thread", "openthread"}, rights = {Rights.ADMINISTRATOR, Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR, Rights.GOLDEN_DONATOR, Rights.EXTREME_DONATOR, Rights.SUPER_DONATOR, Rights.DONATOR, Rights.IRON_MAN, Rights.DESIGNER, Rights.YOUTUBER, Rights.HELPER, Rights.PLAYER}, syntax = "Use this command as ::thread or ::openthread threadId")
public final class OpenThreadCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		int input = Integer.valueOf(cmd[1]);
		
		if(input < 1) {
			player.message("This thread could not be found...");
			return;
		}
		
		player.getMessages().sendLink("community/index.php?/topic/" + input + "-/");
	}
	
}
