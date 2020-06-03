package com.rageps.content.lottery;

/**
 * Created by Jason M on 2017-04-27 at 3:02 PM
 */
public class LotterySessionEntry {

	private final String name;

	private final int tickets;

	public LotterySessionEntry(String name, int tickets) {
		this.name = name;
		this.tickets = tickets;
	}

	public LotterySessionEntry increase(int tickets) {
		return new LotterySessionEntry(name, this.tickets + tickets);
	}

	public String getName() {
		return name;
	}

	public int getTickets() {
		return tickets;
	}
}
