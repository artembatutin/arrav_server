package com.rageps.net.discord;

public enum DiscordRole {
	OWNER(""),
	DEVELOPER(""),
	ADVISOR(""),
	GLOBAL_MODERATOR(""),
	MODERATOR(""),
	SERVER_SUPPORT(""),
	FORUM_MODERATOR(""),
	DISCORD_ADMINISTRATOR("");

	private final String identifier;

	DiscordRole(String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}
}
