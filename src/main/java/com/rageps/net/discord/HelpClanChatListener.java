package com.rageps.net.discord;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.rageps.content.clanchannel.ClanMember;
import com.rageps.content.clanchannel.ClanRepository;
import com.rageps.content.clanchannel.channel.ClanChannel;
import com.rageps.net.discord.cmd.CommandManager;
import com.rageps.util.StringUtil;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.rageps.net.discord.DiscordChannel.HELP_CC_TEXT;
import static com.rageps.net.discord.DiscordChannel.TRADE_CC_TEXT;

/**
 * Created by Ryley Kimmel on 5/31/2017.
 */
public final class HelpClanChatListener extends ListenerAdapter {
	private static final List<String> FORBIDDEN = Arrays.asList("@here", "@everyone");

	private static final int SECONDS_UNTIL_EXPIRE = 1;
	private static final int MAXIMUM_CHARACTERS = 120;

	private static final Cache<Member, Boolean> timeouts = CacheBuilder.newBuilder()
			.expireAfterWrite(SECONDS_UNTIL_EXPIRE, TimeUnit.SECONDS)
			.initialCapacity(1024)
			.build();


	public static void mirrorGlobalDrop(Player player, String content) {
		if (!World.get().getDiscord().isConnected()) {
			return;
		}
		try {
			
			new DiscordWebhook(DiscordWebhook.DiscordBot.CLAN, player.credentials + ": " +content).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
/*
		if (World.getDiscord().getWebhookClient(DiscordWebhook.GLOBAL_DROPS).) {
			System.err.println("Connection is null...");
			return;
		}*
		
		
		/try (WebhookClient client = World.getDiscord().getWebhookClient(DiscordWebhook.GLOBAL_DROPS)) {

			WebhookMessageBuilder messageBuilder = new WebhookMessageBuilder();
			messageBuilder.setUsername(player.getUsername());

			MessageEmbed embed = new EmbedBuilder()
					.setColor(Color.GREEN)
					.setTitle("Global drop")
					.setDescription(StringUtil.sanitizeInput(content))
					.build();
			messageBuilder.addEmbeds(embed);

			WebhookMessage webhookMessage = messageBuilder.build();
			client.send(webhookMessage);
		}*/
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();
		TextChannel channel = message.getTextChannel();

		String channelId = channel.getId();

		String gameClanChat;
		if (channelId.equals(HELP_CC_TEXT.getIdentifier())) {
			gameClanChat = "Help";
		} else if (channelId.equals(TRADE_CC_TEXT.getIdentifier())) {
			gameClanChat = "Trade";
		} else {
			// Don't intercept messages from other channels
			return;
		}

		// Exclude messages from any bots
		if (message.getAuthor().isBot()) {
			return;
		}

		// Don't send commands
		if (message.getContentRaw().startsWith(CommandManager.PREFIX) || message.getContentRaw().startsWith("`")) {
			return;
		}

		// Don't allow mentions
		if (!message.getMentionedUsers().isEmpty() || !message.getMentionedRoles().isEmpty() || message.mentionsEveryone()) {
			MessageUtil.delete(channel, message, "You cannot mention anyone in this channel " + message.getAuthor().getAsMention());
			return;
		}

		Boolean notified = timeouts.getIfPresent(message.getAuthor());
		if (notified != null) {

			// If they have been notified, delete the message
			if (notified) {
				message.delete().queue();
			} else {
				// Otherwise notify and delete
				MessageUtil.delete(channel, message, "You may only send a message once every " + SECONDS_UNTIL_EXPIRE + " second " + message.getAuthor().getAsMention());
				timeouts.put(message.getMember(), true);
			}

			return;
		}


		Optional<ClanChannel> clan = ClanRepository.getChannel(gameClanChat);
		if (!clan.isPresent()) {
			channel.sendMessage("Unable to find the \"" + gameClanChat + "\" clan chat.").queue();
			return;
		}
		sendMessage(channel, clan.get(), message);
	}

	private void sendMessage(TextChannel channel, ClanChannel clan, Message message) {
		String content = message.getContentRaw();

		String bracketColor = "<col=0>";
		String clanNameColor = "<col=0000FF>";
		String nameColor = "<col=0>";
		String chatColor = "<col=800000>";

		Member member = message.getMember();
		content = filterInvalid(content);

		if (content.equals(" ") || content.isEmpty()) {
			MessageUtil.delete(channel, message, "Your message may not be empty, " + message.getAuthor().getAsMention());
			return;
		}

		String sender = member.getEffectiveName();
		sender = StringUtil.capitalizeWords(filterInvalid(sender));
		timeouts.put(member, false);

		for (ClanMember memberPlayer : clan.getMembers()) {
			if (memberPlayer != null && memberPlayer.player.isPresent()) {
				memberPlayer.player.get().message(StringUtil.capitalize(content));
			}
		}
	}

	private static String filterInvalid(String raw) {
		String uncompressed = StringUtil.filterInvalidCharacters(raw);
		uncompressed = StringUtil.capitalize(StringUtil.sanitizeInput(uncompressed));
		uncompressed = uncompressed.substring(0, Math.min(MAXIMUM_CHARACTERS, uncompressed.length()));
		return new String(uncompressed.getBytes(StandardCharsets.UTF_8));
	}

	public static void mirrorClanChatMessage(Player player, String content) {
		try {
			new DiscordWebhook(DiscordWebhook.DiscordBot.CLAN, player.credentials.username + ": " +content).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
