package com.rageps.net.discord.cmd;

import com.rageps.net.discord.Discord;
import net.dv8tion.jda.core.entities.Message;

/**
 * Created by Ryley Kimmel on 1/25/2017.
 */
public final class HelpCommandListener implements CommandListener {
	@Override
	public void execute(Discord discord, Message message, String name, CommandArguments arguments) {
		message.getTextChannel().sendMessage("You don't need any help, fool!").queue();
	}
}
