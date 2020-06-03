package com.rageps.util;

import com.google.common.base.MoreObjects;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ryley Kimmel on 2/20/2017.
 */
public final class Duration {

	private final long millis;

	public Duration(long millis) {
		this.millis = millis;
	}

	public Duration(Temporal start, Temporal end) {
		long until = start.until(end, ChronoUnit.MILLIS);
		if (until < 0) {
			until = end.until(start, ChronoUnit.MILLIS);
		}
		this.millis = until;
	}

	public long getMillis() {
		return millis % 1000;
	}

	public long getSeconds() {
		return TimeUnit.MILLISECONDS.toSeconds(millis) % 60;
	}

	public long getMinutes() {
		return TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
	}

	public long getHours() {
		return TimeUnit.MILLISECONDS.toHours(millis) % 24;
	}

	public long getDays() {
		return TimeUnit.MILLISECONDS.toDays(millis);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("millis", millis).add("seconds", getSeconds()).add("minutes", getMinutes()).add("hours", getHours()).add("days", getDays())
						.toString();
	}
}
