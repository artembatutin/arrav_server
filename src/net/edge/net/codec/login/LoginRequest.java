package net.edge.net.codec.login;

/**
 * The implementation that contains data used for the final portion of the login protocol.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class LoginRequest {

	/**
	 * The username hash.
	 */
	private final long usernameHash;

	/**
	 * The username of the player.
	 */
	private final String username;

	/**
	 * The password of the player.
	 */
	private final String password;

	/**
	 * The mac address of this player.
	 */
	private final String macAddress;

	/**
	 * Creates a new {@link LoginRequest}.
	 * @param usernameHash the username hash.
	 * @param username     the username of the player.
	 * @param password     the password of the player.
	 * @param macAddress   the mac address of the player.
	 */
	public LoginRequest(long usernameHash, String username, String password, String macAddress) {
		this.username = username;
		this.usernameHash = usernameHash;
		this.password = password;
		this.macAddress = macAddress;
	}

	/**
	 * Returns the username hash.
	 */
	public long getUsernameHash() {
		return usernameHash;
	}

	/**
	 * Gets the username of the player.
	 * @return the username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Gets the password of the player.
	 * @return the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Gets the mac address of the player.
	 * @return the mac address.
	 */
	public String getMacAddress() {
		return macAddress;
	}

}
