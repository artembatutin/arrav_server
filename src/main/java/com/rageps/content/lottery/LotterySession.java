package com.rageps.content.lottery;

import com.google.common.collect.ImmutableMap;
import com.rageps.util.rand.RandomUtils;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jason M on 2017-04-27 at 1:30 PM
 */
public class LotterySession {

	/**
	 * The id of the session.
	 */
	private final int id;

	/**
	 * The time that this session ends.
	 */
	private final ZonedDateTime endDate;

	/**
	 * The current set of entries.
	 */
	private final Map<String, LotterySessionEntry> entries;

	/**
	 * The cost of a ticket for this session.
	 */
	private final int ticketCost;

	/**
	 * A mapping of placements to winners.
	 */
	private Map<LotteryPlacement, LotterySessionWinner> winners = new HashMap<>();

	/**
	 * The default winner of the session, if no winner can be determined.
	 */
	public static final Map<LotteryPlacement, LotterySessionWinner> DEFAULT_WINNERS = ImmutableMap.of(
	 	LotteryPlacement.FIRST, new LotterySessionWinner(LotteryPlacement.FIRST, "Logan", 1),
	 	LotteryPlacement.SECOND, new LotterySessionWinner(LotteryPlacement.SECOND, "Is the", 1),
	 	LotteryPlacement.THIRD, new LotterySessionWinner(LotteryPlacement.THIRD, "Bomb", 1)
	 );

	public LotterySession(int id, ZonedDateTime endDate, int ticketCost) {
		this.id = id;
		this.endDate = endDate;
		this.ticketCost = ticketCost;
		this.entries = new HashMap<>();
	}

	public LotterySession(int id, ZonedDateTime endDate, int ticketCost, Map<String, LotterySessionEntry> entries) {
		this(id, endDate, ticketCost);
		this.entries.putAll(entries);
	}

	public boolean isEntriesEmpty() {
		return entries.isEmpty();
	}

	/**
	 * Returns a new instance of this session.
	 *
	 * @return a simple copy of the object.
	 */
	public LotterySession copy() {
		LotterySession session = new LotterySession(id, endDate, ticketCost, entries);

		session.winners.putAll(winners);
		return session;
	}

	@Override
	public String toString() {
		return "Session{" + "id=" + id + ", endDate=" + endDate + ", winner=" + winners + ", entries=" + entries + "}";
	}

	Map<LotteryPlacement, LotterySessionWinner> determineWinners() {
		if (entries.size() < DEFAULT_WINNERS.size()) {
			return DEFAULT_WINNERS;
		}
		List<String> nameEntries = new ArrayList<>();

		for (LotterySessionEntry entry : entries.values()) {
			for (int i = 0; i < entry.getTickets(); i++) {
				nameEntries.add(entry.getName());
			}
		}
		Map<LotteryPlacement, LotterySessionWinner> winners = new HashMap<>();

		for (LotteryPlacement placement : LotteryPlacement.values()) {
			String randomName = RandomUtils.random(nameEntries);

			nameEntries.removeIf(entry -> entry.equals(randomName));

			LotterySessionEntry entry = entries.get(randomName);

			winners.put(placement, new LotterySessionWinner(placement, entry.getName(), entry.getTickets()));
		}
		return winners;
	}

	public long getPot() {
		return (long) getTotalTickets() * (long) getTicketCost();
	}

	public int getPotAfterTax() {
		long pot = getPot();

		return (int) (pot * Lottery.TAX);
	}

	public int getTotalTickets() {
		return entries.values().stream().mapToInt(LotterySessionEntry::getTickets).sum();
	}

	public void putOrAdd(String username, int tickets) {
		LotterySessionEntry current = entries.get(username);

		if (current == null) {
			entries.put(username, new LotterySessionEntry(username, tickets));
		} else {
			entries.put(username, current.increase(tickets));
		}
	}


	public LotterySessionEntry getOrNull(String username) {
		return entries.get(username);
	}


	public void setWinnersIfAbsent(Map<LotteryPlacement, LotterySessionWinner> winners) {
		if (!this.winners.isEmpty()) {
			return;
		}
		this.winners = winners;
	}

	public Map<LotteryPlacement, LotterySessionWinner> getWinners() {
		return winners;
	}

	public int getId() {
		return id;
	}

	public ZonedDateTime getEndDate() {
		return endDate;
	}

	public int getTicketCost() {
		return ticketCost;
	}
}
