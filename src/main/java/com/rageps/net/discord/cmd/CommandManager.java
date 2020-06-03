package com.rageps.net.discord.cmd;

import com.rageps.util.TextUtils;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import com.rageps.net.discord.Discord;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ryley Kimmel on 1/25/2017.
 */
public final class CommandManager extends ListenerAdapter {
public static final String PREFIX = "::";

	private final Map<String, CommandListener> listeners = new HashMap<>();

	private final Discord discord;

	public CommandManager(Discord discord) {
		this.discord = discord;
		init();
	}

	public void init() {
		listeners.clear(); // For reloading

		listeners.put("help", new HelpCommandListener());

		DiscordCommandListener discordCommandListener = new DiscordCommandListener();
		listeners.put("purge", discordCommandListener);
		listeners.put("punish", discordCommandListener);
		listeners.put("restart-game-server", discordCommandListener);

		GeneralCommandListener generalCommandListener = new GeneralCommandListener();
		listeners.put("players", generalCommandListener);
		listeners.put("star", generalCommandListener);
		listeners.put("servertime", generalCommandListener);
		listeners.put("staff", generalCommandListener);
		listeners.put("pm", generalCommandListener);
		listeners.put("topic", generalCommandListener);
		listeners.put("get-item-amount", generalCommandListener);
	}

	public void listen(Message message, String name, String[] arguments) {
		CommandListener listener = listeners.get(name.toLowerCase());
		if (listener == null) {
			return;
		}
		listener.execute(discord, message, name, new CommandArguments(arguments));
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();

		String command = message.getContentRaw();
		if (!command.startsWith(PREFIX)) {
			return;
		}

		String[] components = command.replace(PREFIX, "").split(" ");
		String name = components[0];

		String[] filtered = Arrays.copyOfRange(components, 1, components.length);
		String[] arguments = TextUtils.split(String.join(" ", filtered), '"');
		listen(message, name, arguments);
	}
}
