package com.rageps.net.refactor.codec.login;

import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.Rights;

/**
 * Represents a login response.
 *
 * @author Graham
 */
public final class LoginResponse {

	/**
	 * The flagged flag.
	 */
	private final boolean flagged;

	/**
	 * The login status.
	 */
	private final int status;

	/**
	 * The {@link Player}s authority level.
	 */
	private final Rights rights;

	/**
	 * Creates the login response.
	 *
	 * @param status The login status.
	 * @param flagged The flagged flag.
	 */
	public LoginResponse(int status, Rights rights, boolean flagged) {
		this.status = status;
		this.flagged = flagged;
		this.rights = rights;
	}

	/**
	 * Gets the status.
	 *
	 * @return The status.
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Checks if the player should be flagged.
	 *
	 * @return The flagged flag.
	 */
	public boolean isFlagged() {
		return flagged;
	}

	/**
	 * @return The {@link Player}s authority level.
	 */
	public Rights getRights() {
		return rights;
	}
}