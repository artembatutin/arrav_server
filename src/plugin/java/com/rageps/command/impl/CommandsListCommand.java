package com.rageps.command.impl;

import com.rageps.command.Command;
import com.rageps.command.CommandSignature;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

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
		player.interfaceText(8144, "Commands");
		player.interfaceText(8146, "Home - ::home");
		player.interfaceText(8146, "Train - ::train");
		player.interfaceText(8147, "Vote - ::vote");
		player.interfaceText(8148, "Redeem vote auth - ::redeem auth_code");
		player.interfaceText(8149, "Claims donation - ::claim");
	}
}