package com.rageps.net.codec.login;

import com.rageps.net.codec.crypto.IsaacRandom;

/**
 * A login request to be handled by the service.
 * @author Artem Batutin
 */
public class LoginRequest {
	
	/**
	 * The player's username hash received from client.
	 */
	private final long usernameHash;
	
	/**
	 * The player's username on login.
	 */
	private final String username;
	
	/**
	 * The player's password on login.
	 */
	private final String password;
	
	/**
	 * The message encryptor.
	 */
	private final IsaacRandom encryptor;
	
	/**
	 * The message decryptor.
	 */
	private final IsaacRandom decryptor;
	
	/**
	 * The mac address the connection was received from.
	 */
	private final String macAddress;
	
	/**
	 * Constructs a new {@link LoginRequest}.
	 */
	LoginRequest(long usernameHash, String username, String password, IsaacRandom encryptor, IsaacRandom decryptor, String macAddress) {
		this.usernameHash = usernameHash;
		this.username = username;
		this.password = password;
		this.encryptor = encryptor;
		this.decryptor = decryptor;
		this.macAddress = macAddress;
	}
	
	public long getUsernameHash() {
		return usernameHash;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public IsaacRandom getEncryptor() {
		return encryptor;
	}
	
	public IsaacRandom getDecryptor() {
		return decryptor;
	}
	
	public String getMacAddress() {
		return macAddress;
	}
}
