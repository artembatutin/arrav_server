package net.edge.net.codec.login;

import net.edge.net.codec.crypto.IsaacRandom;

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
	 * The encryptor for encrypting messages.
	 */
	private final IsaacRandom encryptor;
	
	/**
	 * The decryptor for decrypting messages.
	 */
	private final IsaacRandom decryptor;
	
	/**
	 * Creates a new {@link LoginRequest}.
	 * @param usernameHash the username hash.
	 * @param username  the username of the player.
	 * @param password  the password of the player.
	 * @param macAddress the mac address of the player.
	 * @param encryptor the encryptor for encrypting messages.
	 * @param decryptor the decryptor for decrypting messages.
	 */
	LoginRequest(long usernameHash, String username, String password, String macAddress, IsaacRandom encryptor, IsaacRandom decryptor) {
		this.username = username;
		this.usernameHash = usernameHash;
		this.password = password;
		this.macAddress = macAddress;
		this.encryptor = encryptor;
		this.decryptor = decryptor;
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
