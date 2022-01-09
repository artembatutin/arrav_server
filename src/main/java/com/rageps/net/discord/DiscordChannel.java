package com.rageps.net.discord;

/**
 * Created by Ryley Kimmel on 1/24/2017.
 */
public enum DiscordChannel {
	GENERAL_TEXT(""),
	STAFF_TEXT(""),
	LOGS_TEXT(""),
	DEVELOPMENT_TEXT(""),
	HELP_CC_TEXT(""),
	TRADE_CC_TEXT(""),
	GLOBAL_DROPS_TEXT(""),
	BOT_SPAM(""),
	DISCORD_LOGS("");

	private final String identifier;

	DiscordChannel(String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}
}
