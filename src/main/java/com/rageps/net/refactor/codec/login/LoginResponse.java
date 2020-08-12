package com.rageps.net.refactor.codec.login;

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
	 * Creates the login response.
	 *
	 * @param status The login status.
	 * @param flagged The flagged flag.
	 */
	public LoginResponse(int status, boolean flagged) {
		this.status = status;
		this.flagged = flagged;
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

}