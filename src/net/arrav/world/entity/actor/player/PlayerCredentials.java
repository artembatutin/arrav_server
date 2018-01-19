package net.arrav.world.entity.actor.player;

import net.arrav.util.TextUtils;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 8-7-2017.
 */
public final class PlayerCredentials {
	
	public String formattedUsername;
	public long usernameHash;
	public String username;
	public String password;
	
	public PlayerCredentials(String username, String password) {
		this.formattedUsername = TextUtils.capitalize(username);
		this.usernameHash = TextUtils.nameToHash(username);
		this.username = username;
		this.password = password;
	}
	
	public void setUsername(String username) {
		this.username = username;
		this.formattedUsername = TextUtils.capitalize(username);
		this.usernameHash = TextUtils.nameToHash(username);
	}
	
}
