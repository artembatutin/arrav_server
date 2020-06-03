package com.rageps.net.discord;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.concurrent.TimeUnit;

public final class MessageUtil {
	public static void delete(TextChannel channel, Message message, String reply) {
		message.delete().queue(__ -> channel.sendMessage(reply).queue(replied -> replied.delete().queueAfter(5, TimeUnit.SECONDS)));
	}
}
