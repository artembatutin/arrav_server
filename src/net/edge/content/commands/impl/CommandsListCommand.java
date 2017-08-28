package net.edge.content.commands.impl;

import net.edge.content.commands.Command;
import net.edge.content.commands.CommandSignature;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.entity.actor.player.assets.Rights;

/**
 * Created by Dave/Ophion
 * Date: 12/08/2017
 * https://github.com/ophionB | https://www.rune-server.ee/members/ophion/
 */
@CommandSignature(alias = {"commands", "cmd", "command"}, rights = {Rights.ADMINISTRATOR, Rights.SENIOR_MODERATOR, Rights.MODERATOR, Rights.GOLDEN_DONATOR, Rights.EXTREME_DONATOR, Rights.SUPER_DONATOR, Rights.DONATOR, Rights.IRON_MAN, Rights.DESIGNER, Rights.YOUTUBER, Rights.HELPER, Rights.PLAYER}, syntax = "Use this command as ::commands")
public class CommandsListCommand implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.widget(8134);
		player.text(8144, "Commands");
		player.text(8146, "Home - ::home");
		player.text(8146, "Train - ::train");
		player.text(8147, "Vote - ::vote");
		player.text(8148, "Redeem vote auth - ::redeem auth_code");
		player.text(8149, "Claims donation - ::claim");
	}
}