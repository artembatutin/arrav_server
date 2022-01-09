package com.rageps.content.top_pker;

/**
 * Created by Jason M on 2017-03-16 at 1:31 AM
 */
public class Entry {

	/**
	 * The username of the entry as a long.
	 */
	private final long usernameAsLong;

	/**
	 * The kills of the player in the entry.
	 */
	private final int kills;

	/**
	 * The deaths of the player in the entry.
	 */
	private final int deaths;

	/**
	 * Creates a new entry for a given player.
	 *
	 * @param usernameAsLong
	 * 			  the username as a long.
	 * @param kills
	 * 			  the amount of kills.
	 * @param deaths
	 * 			  the amount of deaths.
	 */
	public Entry(long usernameAsLong, int kills, int deaths) {
		this.usernameAsLong = usernameAsLong;
		this.kills = kills;
		this.deaths = deaths;
	}

	/**
	 * The username of the player in the form of a long.
	 *
	 * @return the username as a long type.
	 */
	public long getUsernameAsLong() {
		return usernameAsLong;
	}

	/**
	 * The kills that this entry has obtained.
	 *
	 * @return the amount of kills.
	 */
	public int getKills() {
		return kills;
	}

	/**
	 * The amount of deaths that this entry has obtained.
	 *
	 * @return the amount of deaths.
	 */
	public int getDeaths() {
		return deaths;
	}

	/**
	 * Creates a new entry and returns it by increasing the kills and deaths of this entry by
	 * the given amount.
	 *
	 * @param kills
	 * 			  the kills to increase by.
	 * @param deaths
	 * 			  the deaths to increase by.
	 * @return the new entry with the increase in kills and deaths.
	 */
	Entry increase(int kills, int deaths) {
		return new Entry(usernameAsLong, this.kills + kills, this.deaths + deaths);
	}
}
