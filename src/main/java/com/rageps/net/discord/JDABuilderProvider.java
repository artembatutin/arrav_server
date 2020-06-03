package com.rageps.net.discord;

import com.rageps.GameConstants;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by Ryley Kimmel on 1/24/2017.
 */
public final class JDABuilderProvider {

	private final String apiKey;
	private final String serverId;

	public JDABuilderProvider(String apiKey, String serverId) {
		this.apiKey = apiKey;
		this.serverId = serverId;
	}

	public static JDABuilderProvider create() {
		Properties properties = new Properties();
		try {
			properties.load(Files.newBufferedReader(Paths.get(GameConstants.SAVE_DIRECTORY, "etc", "discord", "config.ini")));
		} catch (IOException cause) {
			throw new UncheckedIOException("Unable to load discord config.ini properties. Does it exist?", cause);
		}

		return new JDABuilderProvider(properties.getProperty("apiKey"), properties.getProperty("serverId"));
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getServerId() {
		return serverId;
	}

	public JDABuilder provide() {
		return new JDABuilder(AccountType.BOT).setToken(apiKey);
	}
}
