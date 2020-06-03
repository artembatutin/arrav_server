package com.rageps.content.top_pker;


import com.rageps.util.NumberUtil;
import com.rageps.util.StringUtil;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A unique session defined by the identification value {@link TopPkerSession#id}. A session exists until the current
 * date is logically after the {@link TopPkerSession#endDate}. At which point a winner is determined based on the entries
 * in the sql server. By default a pre-defined winner is inserted into the entries for a new session to ensure that
 * a winner can be determined.
 *
 */
public class TopPkerSession {

	/**
	 * The id of the session.
	 */
	private final int id;

	/**
	 * The time that this session ends.
	 */
	private final ZonedDateTime endDate;

	/**
	 * The winner of the session, if any.
	 */
	private Optional<Winner> winner = Optional.empty();

	/**
	 * The current set of entries.
	 */
	private final Map<Long, Entry> entries;

	/**
	 * The default winner of the session, if no winner can be determined.
	 */
	public static final Winner DEFAULT_WINNER = new Winner(StringUtil.stringToLong("tamatea"), 1, 0);

	public TopPkerSession(int id, ZonedDateTime endDate) {
		this.id = id;
		this.endDate = endDate;
		this.entries = new HashMap<>();
	}

	public TopPkerSession(int id, ZonedDateTime endDate, Map<Long, Entry> entries) {
		this.id = id;
		this.endDate = endDate;
		this.entries = entries;
	}

	/**
	 * Returns a new instance of this session.
	 *
	 * @return a simple copy of the object.
	 */
	public TopPkerSession copy() {
		TopPkerSession session = new TopPkerSession(id, endDate, entries);

		session.winner = winner;

		return session;
	}

	@Override
	public String toString() {
		return "Session{" + "id=" + id + ", endDate=" + endDate + ", winner=" + winner + ", entries=" + entries + "}";
	}

	/**
	 * Determines a winner based on the given comparator passed.
	 *
	 * @return the winner of this session.
	 */
	Winner determineWinner(Comparator<Entry> comparator) {
		Entry entry = new ArrayList<>(entries.values()).stream().max(comparator).orElse(null);

		return entry == null ? DEFAULT_WINNER : new Winner(entry.getUsernameAsLong(), entry.getKills(), entry.getDeaths());
	}

	/**
	 * Determines a winner based on comparing by kills.
	 *
	 * @return a new winner based
	 */
	Winner determineWinner() throws TopPker.WinnerExistsException {
		return determineWinner(COMPARE_BY_KILLS);
	}

	/**
	 * The best entries in the entries list, with the maximum amount returned.
	 *
	 * @param maximumEntries
	 * 			  the max amount of entries we expect.
	 * @return the best entries.
	 */
	List<Entry> bestEntries(int maximumEntries) {
		return entries.values().stream().sorted(COMPARE_BY_KILLS.reversed()).limit(maximumEntries).collect(Collectors.toList());
	}

	/**
	 * Puts a new entry into the {@link #entries} mapping for the given username.
	 *
	 * @param usernameAsLong
	 * 			  the unique player username as a long value.
	 * @param kills
	 * 			  the kills the player has obtained.
	 * @param deaths
	 * 			  the deaths the player has accumulated.
	 */
	public void putOrAdd(long usernameAsLong, int kills, int deaths) {
		Entry current = entries.get(usernameAsLong);

		if (current == null) {
			entries.put(usernameAsLong, new Entry(usernameAsLong, kills, deaths));
		} else {
			entries.put(usernameAsLong, current.increase(kills, deaths));
		}
	}

	/**
	 * Obtains the entry for the given username, or null.
	 *
	 * @param usernameAsLong
	 * 			   the username of the entry.
	 * @return the entry, or null.
	 */
	public Entry getOrNull(long usernameAsLong) {
		return entries.get(usernameAsLong);
	}

	/**
	 * Sets the current winner to that of the parameter if no current winner is present.
	 *
	 * @param winner
	 * 			  the new winner.
	 * @throws TopPker.WinnerExistsException
	 * 			   thrown if there is already a winner in this session.
	 */
	public void setWinnerIfAbsent(Winner winner) throws TopPker.WinnerExistsException {
		if (this.winner.isPresent()) {
			throw new TopPker.WinnerExistsException();
		}
		this.winner = Optional.of(winner);
	}

	/**
	 * The winner of this session, or none if there is no winner.
	 *
	 * @return the winner, or empty.
	 */
	public Optional<Winner> getWinner() {
		return winner;
	}

	/**
	 * The mapping of entries in this session for the respective long value representing the username
	 * and the value being the entry containing kills and deaths.
	 *
	 * @return the entries for this session.
	 */
	public final Map<Long, Entry> getEntries() {
		return entries;
	}

	/**
	 * Compares two entries by kills. Then will compare them by the kill to death ratio.
	 */
	private static final Comparator<Entry> COMPARE_BY_KILLS = Comparator.comparingInt(Entry::getKills).thenComparingDouble(entry -> NumberUtil.percentage(entry.getKills(), entry.getDeaths()));

	public int getId() {
		return id;
	}

	public ZonedDateTime getEndDate() {
		return endDate;
	}
}
