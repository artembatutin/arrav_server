package com.rageps.net.discord;

import com.rageps.world.World;
import com.rageps.world.env.Environment;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDA.Status;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.rageps.net.discord.cmd.CommandManager;

import java.util.List;

/**
 * Created by Ryley Kimmel on 1/24/2017.
 */
public final class Discord {
	private static final Logger logger = LogManager.getLogger(Discord.class);

	private JDABuilderProvider provider;
	private JDABuilder builder;
	private CommandManager commandManager;

	private boolean connected;
	private JDA discord;

	public Discord() {
		if (World.get().getEnvironment().getType() != Environment.Type.LIVE) {
			logger.info("Discord service not started, server not initialized in live environment.");
			return;
		}
	
		provider = JDABuilderProvider.create();
		builder = provider.provide();
		commandManager = new CommandManager(this);
	
		builder.setGame(Game.of(Game.GameType.WATCHING, World.get().getEnvironment().getName(), "https://Vorkath.net/"));
		builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
		builder.addEventListener(new HelpClanChatListener());
		builder.addEventListener(commandManager);
		//builder.addEventListener(new TextChatListener(this, DiscordChannel.BOT_SPAM,
			//	DiscordChannel.HELP_CC_TEXT, DiscordChannel.TRADE_CC_TEXT, DiscordChannel.GENERAL_TEXT, DiscordChannel.STAFF_TEXT));

		builder.setAutoReconnect(true);

//		builder.setReconnectQueue(new SessionReconnectQueue());
	}

	public void init() {
		if (World.get().getEnvironment().getType() != Environment.Type.LIVE) {
			return;
		}

		try {
			discord = builder.buildBlocking(Status.CONNECTED);
			connected = true;
		} catch (Exception cause) {
			logger.error("Discord service unable to be started.", cause);
			return;
		}

		discord.setAutoReconnect(true);
		logger.info("Discord service started. Auto-reconnect enabled: {}", discord.isAutoReconnect());
	}

	public void sendMessage(DiscordChannel channel, String message) {
		if (isConnected()) {
			getChannel(channel).sendMessage(message).queue();
		}
	}
//what do you use to use commands, like a /? . , [ ] ; . ! etc idk tbh, it probably does work, i just did it wrong
	public boolean hasRole(User user, DiscordRole role) {
		Guild guild = getServer();
		List<Role> roles = guild.getMember(user).getRoles();
		return roles.contains(guild.getRoleById(role.getIdentifier()));
	}

	protected TextChannel getChannel(DiscordChannel channel) {
		return discord.getTextChannelById(channel.getIdentifier());
	}

	public Guild getServer() {
		return discord.getGuildById(provider.getServerId());
	}

	public JDA getApi() {
		return discord;
	}

	public boolean isConnected() {
		return discord != null && connected;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}
}
