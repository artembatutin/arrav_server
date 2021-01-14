package com.rageps.net.refactor.codec.login;

import com.rageps.net.refactor.security.IsaacRandomPair;
import com.rageps.world.entity.actor.player.PlayerCredentials;

/**
 * Represents a login request.
 *
 * @author Graham
 */
public final class LoginRequest {

	/**
	 * The version denoting whether the client has been modified or not.
	 */
	private final int clientVersion;

	/**
	 * The player's credentials.
	 */
	private final PlayerCredentials credentials;


	/**
	 * The pair of random number generators.
	 */
	private final IsaacRandomPair randomPair;

	/**
	 * The reconnecting flag.
	 */
	private final boolean reconnecting;


	/**
	 * Creates a login request.
	 *
	 * @param credentials The player credentials.
	 * @param randomPair The pair of random number generators.
	 * @param reconnecting The reconnecting flag.
	 * @param clientVersion The client version.
	 */
	public LoginRequest(PlayerCredentials credentials, IsaacRandomPair randomPair,  boolean reconnecting, int clientVersion) {
		this.credentials = credentials;
		this.randomPair = randomPair;
		this.reconnecting = reconnecting;
		this.clientVersion = clientVersion;
	}



	/**
	 * Gets the value denoting the client's (modified) version.
	 *
	 * @return The client version.
	 */
	public int getClientVersion() {
		return clientVersion;
	}

	/**
	 * Gets the player's credentials.
	 *
	 * @return The player's credentials.
	 */
	public PlayerCredentials getCredentials() {
		return credentials;
	}

	/**
	 * Gets the pair of random number generators.
	 *
	 * @return The pair of random number generators.
	 */
	public IsaacRandomPair getRandomPair() {
		return randomPair;
	}

	/**
	 * Checks if this client is reconnecting.
	 *
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isReconnecting() {
		return reconnecting;
	}

}