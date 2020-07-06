package com.rageps.util;

import java.util.concurrent.TimeUnit;

public final class TickUnit {

	private static final long TICK_INTERVAL = 600L;

	public static long toDays(long ticks) {
		return toHours(ticks) / 24;
	}

	public static long toHours(long ticks) {
		return toMinutes(ticks) / 60;
	}

	public static long toMinutes(long ticks) {
		return toSeconds(ticks) / 60;
	}

	public static long toSeconds(long ticks) {
		return (long) Math.ceil(ticks * TICK_INTERVAL / 1000);
	}

	public static int ticks(long interval, TimeUnit unit) {
		long millis = unit.toMillis(interval);
		long ticks = (long) Math.floor(millis / TICK_INTERVAL);
		if (ticks > Integer.MAX_VALUE) {
			throw new UnsupportedOperationException("ticks for interval: " + interval + " at: " + unit + " cannot be larger than Integer.MAX_VALUE");
		}
		return (int) ticks;
	}

}
