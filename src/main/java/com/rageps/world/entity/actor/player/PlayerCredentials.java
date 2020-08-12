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
	private String hostAddress;
	private String macAddress;



	public int databaseId;
	private PlayerEmail playerEmail;

	public PlayerCredentials(String username, String password) {
		this.formattedUsername = TextUtils.capitalize(username);
		this.usernameHash = TextUtils.nameToHash(username);
		this.username = username;
		this.password = password;
	}

	public PlayerCredentials(String username, String password, long usernameHash, String mac, String hostAddress) {
		this.username = username;
		this.password = password;
		this.usernameHash = usernameHash;
		this.macAddress = mac;
		this.hostAddress = hostAddress;
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

	public String getUsername() {
		return username;
	}

	public int getDatabaseId() {
		return databaseId;
	}

	public long getUsernameHash() {
		return usernameHash;
	}

	public PlayerEmail getPlayerEmail() {
		return playerEmail;
	}

	public String getFormattedUsername() {
		return formattedUsername;
	}

	public String getHostAddress() {
		return hostAddress;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public String getPassword() {
		return password;
	}
}
