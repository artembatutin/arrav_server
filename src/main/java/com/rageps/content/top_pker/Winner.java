package com.rageps.content.top_pker;

/**
 * Represents a player that has won the game. A winner is simply an entry
 * that has become a winner.
 */
public final class Winner extends Entry {

	/**
	 * Creates a new winner from the username long.
	 *
	 * @param usernameAsLong
	 * 			  the username as a long.
	 * @param kills
	 * 			  the amount of kills.
	 * @param deaths
	 * 			  the amount of deaths.
	 */
	public Winner(long usernameAsLong, int kills, int deaths) {
		super(usernameAsLong, kills, deaths);
	}
}
