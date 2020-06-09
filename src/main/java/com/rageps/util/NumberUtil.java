package com.rageps.util;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Created by Ryley Kimmel on 2/18/2017.
 */
public final class NumberUtil {
	private static final NumberFormat DECIMAL_FORMAT = NumberFormat.getInstance(Locale.US);
	private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);

	private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

	/** Gets a percentage amount. */
	public static double getPercentageAmount(int progress, int total) {
		return 100 * progress / total;
	}

	static {
		suffixes.put(1_000L, "k");
		suffixes.put(1_000_000L, "M");
		suffixes.put(1_000_000_000L, "B");
		suffixes.put(1_000_000_000_000L, "T");
		suffixes.put(1_000_000_000_000_000L, "P");
		suffixes.put(1_000_000_000_000_000_000L, "E");

		// set the maximum amount of digits to format for the {@code DECIMAL_FORMAT}
		// used so we don't get values like 1.0222222223, rather 1.02
		DECIMAL_FORMAT.setMaximumFractionDigits(3);
	}

	public static double percentage(int n, int d) {
		if (d == 0) {
			return n;
		}

		return n / d;
	}

	public static String formatDecimal(double d) {
		return DECIMAL_FORMAT.format(d);
	}

	public static String truncate(long value) {
		if (value == Long.MIN_VALUE) {
			return truncate(Long.MIN_VALUE + 1);
		}
		if (value < 0) {
			return "-" + truncate(Math.abs(value));
		}
		if (value < 1000) {
			return Long.toString(value); // deal with easy case
		}

		Map.Entry<Long, String> e = suffixes.floorEntry(value);
		Long divideBy = e.getKey();
		String suffix = e.getValue();

		long truncated = value / (divideBy / 10); // the number part of the output times 10
		boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
		return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
	}

	public static String format(String num) {
		return NumberFormat.getInstance().format(num);
	}

	public static String format(long num) {
		return NumberFormat.getInstance().format(num);
	}

	public static String formatCurrency(String num) {
		return CURRENCY_FORMAT.format(num);
	}

	public static String formatCurrency(double num) {
		return CURRENCY_FORMAT.format(num);
	}

	public static String formatCurrency(long num) {
		return CURRENCY_FORMAT.format(num);
	}

}
