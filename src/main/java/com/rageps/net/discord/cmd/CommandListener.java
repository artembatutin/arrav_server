package com.rageps.net.discord.cmd;

import com.rageps.net.discord.Discord;
import net.dv8tion.jda.core.entities.Message;
import com.rageps.net.discord.Discord;

/**
 * Created by Ryley Kimmel on 1/25/2017.
 */
public interface CommandListener {
	void execute(Discord discord, Message message, String name, CommandArguments arguments);
}
