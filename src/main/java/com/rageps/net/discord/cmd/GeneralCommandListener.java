package com.rageps.net.discord.cmd;

import com.rageps.util.DateTimeUtil;
import com.rageps.util.StringUtil;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import com.rageps.net.discord.Discord;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Created by Ryley Kimmel on 1/25/2017.
 */
public final class GeneralCommandListener implements CommandListener {

	@Override
	public void execute(Discord discord, Message message, String name, CommandArguments arguments) {
		switch (name) {
			case "players":
				message.getTextChannel().sendMessage("There are currently " + World.get().getPlayers().size() + " players in-game on " + World.get().getEnvironment().getName() + ".").queue();
				return;

			case "servertime":
				message.getTextChannel().sendMessage("It is currently " + DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now(DateTimeUtil.ZONE)) + " server time.").queue();
				return;

			case "topic":
				if (!arguments.hasRemaining(1)) {
					message.getTextChannel().sendMessage("You must specify a topic id.").queue();
					return;
				}

				message.getTextChannel().sendMessage("https://www.Vorkath.net/" + arguments.getNext()).queue();
				return;
				
			case "vote":
				message.getTextChannel().sendMessage("https://Vorkath.net");
				return;

			case "pm":
				if (!arguments.hasRemaining(2)) {
					message.getTextChannel().sendMessage("You must specify a username and message to send!").queue();
					return;
				}

				String username = arguments.getNext();
				Player other = World.get().getPlayerByNames().get(StringUtil.stringToLong(username));
				if (other == null) {
					message.getTextChannel().sendMessage("Player: " + username + " is not currently online.").queue();
					return;
				}

				Member member = message.getMember();
				byte[] bytes = arguments.getRemaining().toLowerCase(Locale.US).getBytes(StandardCharsets.UTF_8);
				String uncompressed = StringUtil.decompress(bytes, bytes.length);
				uncompressed = StringUtil.filterInvalidCharacters(uncompressed);
				uncompressed = StringUtil.capitalize(uncompressed);

				byte[] recompressed = new byte[bytes.length];
				StringUtil.compress(uncompressed, recompressed);

				String sender = member.getEffectiveName();
				other.getPrivateMessage().sendPrivateMessage(StringUtil.stringToLong(sender), recompressed, bytes.length);
				message.getTextChannel().sendMessage("Sent.").queue();
		}
	}
}
