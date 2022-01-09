package com.rageps.net.discord.cmd;

import com.rageps.net.discord.Discord;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Ryley Kimmel on 1/25/2017.
 */
public final class DiscordCommandListener implements CommandListener {

	@Override
	public void execute(Discord discord, Message message, String name, CommandArguments arguments) {
		switch (name) {
			case "reload-commands":
				message.delete().queue();

				if (!message.getMember().hasPermission(Permission.ADMINISTRATOR)) {
					message.getTextChannel().sendMessage("No. (Requires elevated privileges)").queue();
					return;
				}

				discord.getCommandManager().init();
				message.getTextChannel().sendMessage("Commands reloaded!").queue();
				return;

			case "purge":
				message.delete().queue();

				if (!message.getMember().hasPermission(Permission.ADMINISTRATOR)) {
					message.getTextChannel().sendMessage("No. (Requires elevated privileges)").queue();
					return;
				}

				if (!arguments.hasRemaining(1)) {
					message.getTextChannel().sendMessage("You must specify an amount of messages to delete [1-100].").queue();
					return;
				}

				int amount;
				try {
					amount = arguments.getNextInteger();
				} catch (NumberFormatException cause) {
					message.getTextChannel().sendMessage(arguments.get() + " is not an integer.").queue();
					return;
				}

				message.getTextChannel().getHistory().retrievePast(amount).queue(msgsRaw -> {
					List<Message> msgs = msgsRaw.stream().filter(m -> !m.getCreationTime().plusWeeks(2).isBefore(OffsetDateTime.now())).collect(Collectors.toList());
					message.getTextChannel().sendMessage("Purging...").queue(msg -> {
						if (msgs.isEmpty()) {
							msg.editMessage("Either all the messages I've found are 2+ weeks old, or there are no messages to purge at all.").queue();
							return;
						}

						if (msgs.size() == 1) {
							msgs.get(0).delete().queue(delet -> msg.editMessage("Purge success `" + msgs.size() + "`").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS)));
						} else {
							message.getTextChannel().deleteMessages(msgs).queue(delet -> msg.editMessage("Purge success `" + msgs.size() + "`").queue(m -> m.delete().queueAfter(5, TimeUnit.SECONDS)));
						}
					});
				});
				return;

		}
	}

}
