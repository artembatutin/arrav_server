package net.edge.world.content.scoreboard;

import net.edge.utils.MutableNumber;

/**
 * The player statistic input for the scoreboard.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class PlayerScoreboardStatistic {

	/**
	 * The username for this statistic holder.
	 */
	private final String username;
	
	/**
	 * The amount of kills for this statistic holder.
	 */
	private final MutableNumber kills;
	
	/**
	 * The amount of deaths for this statistic holder.
	 */
	private final MutableNumber deaths;
	
	/**
	 * The current killstreak for this statistic holder.
	 */
	private final MutableNumber currentKillstreak;
	
	/**
	 * The highest killstreak for this statistic holder.
	 */
	private final MutableNumber highestKillstreak;
	
	/**
	 * Constructs a new {@link PlayerScoreboardStatistic}.
	 * @param username			{@link #username}.
	 * @param highestKillstreak	{@link #highestKillstreak}.
	 * @param currentKillstreak	{@link #currentKillstreak}.
	 * @param kills				{@link #kills}.
	 * @param deaths			{@link #deaths}.
	 */
	public PlayerScoreboardStatistic(String username, int highestKillstreak, int currentKillstreak, int kills, int deaths) {
		this.username = username;
		this.highestKillstreak = currentKillstreak > highestKillstreak ? new MutableNumber(currentKillstreak) : new MutableNumber(highestKillstreak);
		this.currentKillstreak = new MutableNumber(currentKillstreak);
		this.kills = new MutableNumber(kills);
		this.deaths = new MutableNumber(deaths);
	}
	
	/**
	 * Constructs a new {@link PlayerScoreboardStatistic}.
	 * @param username {@link #username}.
	 */
	public PlayerScoreboardStatistic(String username) {
		this(username, 0, 0, 0, 0);
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the kills
	 */
	public MutableNumber getKills() {
		return kills;
	}

	/**
	 * @return the deaths
	 */
	public MutableNumber getDeaths() {
		return deaths;
	}

	/**
	 * @return the highest killstreak
	 */
	public MutableNumber getHighestKillstreak() {
		return highestKillstreak;
	}
	
	/**
	 * @return the current killstreak
	 */
	public MutableNumber getCurrentKillstreak() {
		return currentKillstreak;
	}
	
}
