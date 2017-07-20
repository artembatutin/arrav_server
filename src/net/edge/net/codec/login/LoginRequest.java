package net.edge.net.codec.login;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
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
	 * The build number of the player connecting.
	 */
	private final int build;
	
	/**
	 * The encryptor for encrypting messages.
	 */
	private final IsaacRandom encryptor;
	
	/**
	 * The decryptor for decrypting messages.
	 */
	private final IsaacRandom decryptor;
	
	/**
	 * The pipeline for the underlying {@link Channel}.
	 */
	private final ChannelPipeline pipeline;
	
	/**
	 * Creates a new {@link LoginRequest}.
	 * @param username  the username of the player.
	 * @param password  the password of the player.
	 * @param build     the build number of the player connecting.
	 * @param encryptor the encryptor for encrypting messages.
	 * @param decryptor the decryptor for decrypting messages.
	 */
	LoginRequest(String username, long usernameHash, String password, int build, IsaacRandom encryptor, IsaacRandom decryptor, ChannelPipeline pipeline) {
		this.username = username;
		this.usernameHash = usernameHash;
		this.password = password;
		this.build = build;
		this.encryptor = encryptor;
		this.decryptor = decryptor;
		this.pipeline = pipeline;
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
	 * Gets the build number of the player's client.
	 * @return the build number of the client.
	 */
	public int getBuild() {
		return build;
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
	
	/**
	 * @return The pipeline for the underlying {@link Channel}.
	 */
	public ChannelPipeline getPipeline() {
		return pipeline;
	}
}
