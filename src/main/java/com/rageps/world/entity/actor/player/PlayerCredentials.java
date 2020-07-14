package com.rageps.world.entity.actor.player;

import com.rageps.util.TextUtils;
import com.rageps.world.entity.actor.player.assets.PlayerEmail;

import java.time.LocalDateTime;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 8-7-2017.
 */
public final class PlayerCredentials {
	
	public String formattedUsername;
	public long usernameHash;
	public String username;
	public String password;
	public int databaseId;
	private PlayerEmail playerEmail;

	public PlayerCredentials(String username, String password) {
		this.formattedUsername = TextUtils.capitalize(username);
		this.usernameHash = TextUtils.nameToHash(username);
		this.username = username;
		this.password = password;
	}

	public void setEmail(PlayerEmail playerEmail) {
		this.playerEmail = playerEmail;
	}

	public PlayerEmail getEmail() {
		return playerEmail;
	}

	public void setUsername(String username) {
		this.username = username;
		this.formattedUsername = TextUtils.capitalize(username);
		this.usernameHash = TextUtils.nameToHash(username);
	}
	
}
