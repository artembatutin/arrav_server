package com.rageps.net.sql.daily_statistics;

import java.time.ZonedDateTime;

/**
 * Created by Jason M on 2017-04-03 at 1:09 PM
 */
public class DailyStatisticCurrentRow {

	private final String username;

	private final ZonedDateTime timestamp;

	private final boolean newAccount;

	public DailyStatisticCurrentRow(String username, ZonedDateTime timestamp, boolean newAccount) {
		this.username = username;
		this.timestamp = timestamp;
		this.newAccount = newAccount;
	}

	public String getUsername() {
		return username;
	}

	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public boolean isNewAccount() {
		return newAccount;
	}
}
