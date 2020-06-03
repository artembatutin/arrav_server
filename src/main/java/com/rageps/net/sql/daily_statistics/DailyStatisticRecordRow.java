package com.rageps.net.sql.daily_statistics;

import java.time.ZonedDateTime;

/**
 * Created by Jason M on 2017-04-03 at 1:37 PM
 *
 * Represents a record in the table.
 */
public class DailyStatisticRecordRow {

	/**
	 * The number of new accounts for this day.
	 */
	private final int newAccounts;

	/**
	 * The number of return accounts.
	 */
	private final int returnAccounts;

	/**
	 * The timestamp the record was created.
	 */
	private final ZonedDateTime timestamp;

	public DailyStatisticRecordRow(ZonedDateTime timestamp, int newAccounts, int returnAccounts) {
		this.timestamp = timestamp;
		this.newAccounts = newAccounts;
		this.returnAccounts = returnAccounts;
	}

	/**
	 * The timestamp that the record was created.
	 *
	 * @return the timestamp.
	 */
	public final ZonedDateTime getTimestamp() {
		return timestamp;
	}

	/**
	 * The number of new accounts.
	 *
	 * @return number of new accounts.
	 */
	public int getNewAccounts() {
		return newAccounts;
	}

	/**
	 * The number of return accounts.
	 *
	 * @return number of return accounts.
	 */
	public int getReturnAccounts() {
		return returnAccounts;
	}
}
