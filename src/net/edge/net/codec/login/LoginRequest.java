package net.edge.net.codec.login;

import net.edge.net.codec.crypto.IsaacRandom;

/**
 * The implementation that contains data used for the final portion of the login protocol.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class LoginRequest {
	
	/**
	 * The username of the player.
	 */
	private final String username;

	/**
	 * The username hash.
	 */
	private final long usernameHash;
	
	/**
	 * The password of the player.
	 */
	private final String password;
	
	/**
	 * The encryptor for encrypting messages.
	 */
	private final IsaacRandom encryptor;
	
	/**
	 * The decryptor for decrypting messages.
	 */
	private final IsaacRandom decryptor;
	
	/**
	 * Creates a new {@link LoginRequest}.
	 * @param username  the username of the player.
	 * @param password  the password of the player.
	 * @param encryptor the encryptor for encrypting messages.
	 * @param decryptor the decryptor for decrypting messages.
	 */
	LoginRequest(String username, long usernameHash, String password, IsaacRandom encryptor, IsaacRandom decryptor) {
		this.username = username;
		this.usernameHash = usernameHash;
		this.password = password;
		this.encryptor = encryptor;
		this.decryptor = decryptor;
	}
	
	/**
	 * Gets the username of the player.
	 * @return the username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns the username hash.
	 */
	public long getUsernameHash() {
		return usernameHash;
	}
	
	/**
	 * Gets the password of the player.
	 * @return the password.
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Gets the encryptor for encrypting messages.
	 * @return the encryptor.
	 */
	public IsaacRandom getEncryptor() {
		return encryptor;
	}
	
	/**
	 * Gets the decryptor for decrypting messages.
	 * @return the decryptor.
	 */
	public IsaacRandom getDecryptor() {
		return decryptor;
	}

}
