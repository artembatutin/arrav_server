package com.rageps.util;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Created by Ryley Kimmel on 2/20/2017.
 */
public final class DateTimeUtil {

	/**
	 * A short readable date time format.
	 */
	public static final DateTimeFormatter SHORT_READABLE_FORMAT = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT);

	/**
	 * A readable date time format.
	 */
	public static final DateTimeFormatter READABLE_FORMAT = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT);

	/**
	 * Represents our local time zone, should be used with all date operations to provide consistency.
	 */
	public static final ZoneId ZONE = ZoneId.of("America/New_York");

	/**
	 * Represents the clock for our local time zone, should be used with all instant operations to provide consistency.
	 */
	public static final Clock CLOCK = Clock.system(ZONE);

	/**
	 * Gets the {@link DayOfWeek} for today in our {@link #ZONE}.
	 *
	 * @return Today represented as {@link DayOfWeek}.
	 */
	public static DayOfWeek today() {
		return LocalDate.now(CLOCK).getDayOfWeek();
	}

}
