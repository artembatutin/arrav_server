package com.rageps.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.text.NumberFormat;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * String utilities.
 *
 * @author relex lawl
 */

public class StringUtil {

	/**
	 * An array containing valid characters that may be used on the server.
	 */
	public static final char[] VALID_CHARACTERS = {'_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
			'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+', '=', ':', ';', '.', '>', '<', ',', '"', '[', ']', '|', '?',
			'/', '`'
	};

	public static final char[] FREQUENCY_ORDERED_CHARS = {' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j',
			'q', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%', '"',
			'[', ']'
	};

	private static final Pattern COLOR_PATTERN = Pattern.compile(Pattern.quote("@") + "\\w{3,}" + Pattern.quote("@"));
	private static final Pattern START_BRACKET_PATTERN = Pattern.compile(Pattern.quote("<") + "(trans|col|img|shad|u|str)=\\w+" + Pattern.quote(">"));
	private static final Pattern END_BRACKET_PATTERN = Pattern.compile(Pattern.quote("<") + "/(trans|col|img|shad|u|str)" + Pattern.quote(">"));
	private static final Pattern REQUEST_PATTERN = Pattern.compile(Pattern.quote(":") + "(trade|duel)req" + Pattern.quote(":"));

	public static String sanitizeInput(String input) {
		Matcher matcher = COLOR_PATTERN.matcher(input);
		input = matcher.replaceAll("");

		matcher = START_BRACKET_PATTERN.matcher(input);
		input = matcher.replaceAll("");

		matcher = END_BRACKET_PATTERN.matcher(input);
		input = matcher.replaceAll("");

		matcher = REQUEST_PATTERN.matcher(input);
		input = matcher.replaceAll("");

		return input.trim();
	}

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?");
	}

	/**
	 * Capitalizes every word delimited by a space, {@code \s}
	 */
	public static String capitalizeWords(String name) {
		return WordUtils.capitalize(name.toLowerCase());
	}

	/**
	 * Capitalizes the <strong>FIRST</strong> character in this String and appends the remainder as lower-case.
	 */
	public static String capitalizeFirst(String string) {
		return StringUtils.capitalize(string.toLowerCase());
	}

	public static String getIndefiniteArticle(String s) {
		s = s.toLowerCase();
		if (s.endsWith("worm") || s.endsWith("worms") || s.equals("feathers") || s.equals("anchovies") || s.equals("soft clay") || s.equals("cheese") || s.equals("ball of wool")
				|| s.equals("spice") || s.equals("steel nails") || s.equals("snape grass") || s.equals("coal")) {
			return "some";
		}
		if (s.startsWith("a") || s.startsWith("e") || s.startsWith("i") || s.startsWith("o") || s.startsWith("u")) {
			return "an";
		}
		return "a";
	}


	/**
	 * Checks if a name is valid according to the {@code VALID_PLAYER_CHARACTERS} array.
	 *
	 * @param name The name to check.
	 * @return The name is valid.
	 */
	public static boolean isValidName(String name) {
		return encodeUsername(name).matches("[a-z0-9_]+");
	}

	/**
	 * Converts a name to a long value.
	 *
	 * @param string The string to convert to long.
	 * @return The long value of the string.
	 */
	public static long stringToLong(String string) {
		long l = 0L;
		for (int i = 0; i < string.length() && i < 12; i++) {
			char c = string.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z')
				l += (1 + c) - 65;
			else if (c >= 'a' && c <= 'z')
				l += (1 + c) - 97;
			else if (c >= '0' && c <= '9')
				l += (27 + c) - 48;
		}
		while (l % 37L == 0L && l != 0L)
			l /= 37L;
		return l;
	}

	/**
	 * Converts the long value of a username to a string. It should be noted that
	 * if the username before being converted to a long contained spaces, it will
	 * contain "_" where spaces should be. This needs to be followed with a call
	 * to <b>longToString(value).replaceAll("_", " )</b>.
	 *
	 * @param l
	 * 			  the username in the form of a long value.
	 * @return the username in the form of a String value.
	 */
	public static String longToString(long l) {
		int i = 0;
		char ac[] = new char[12];
		while (l != 0L) {
			long l1 = l;
			l /= 37L;
			ac[11 - i++] = VALID_CHARACTERS[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	/**
	 * Formats a name for use in the protocol.
	 *
	 * @param name The name to format.
	 * @return The formatted name.
	 */
	public static String encodeUsername(String name) {
		return name.toLowerCase().replace(" ", "_");
	}

	public static String decodeUsername(String name) {
		return name.toLowerCase().replace("_", " ");
	}

	public static String prettifyUsername(long username) {
		return capitalizeFirst(decodeUsername(longToString(username)));
	}
	public static String formatName(final String string) {
		return Stream.of(string.trim().split("\\s")).filter(word -> word.length() > 0)
				.map(word -> word.substring(0, 1).toUpperCase() + word.substring(1)).collect(Collectors.joining(" "));
	}

	public static String formatEnumString(String string) {
		return capitalizeFirst(decodeUsername(string));
	}

	public static String formatEnumString(Enum<?> value) {
		return capitalizeFirst(decodeUsername(value.name()));
	}

	/**
	 * Capitalizes the first character in the specified {@code string} and every first character after punctuation. ('.', '?', '!')
	 */
	public static String capitalize(String string) {
		boolean capitalize = true;
		StringBuilder builder = new StringBuilder(string);
		int length = string.length();

		for (int index = 0; index < length; index++) {
			char character = builder.charAt(index);

			if (character == '.' || character == '!' || character == '?') {
				capitalize = true;
			} else if (capitalize && !Character.isWhitespace(character)) {
				builder.setCharAt(index, Character.toUpperCase(character));
				capitalize = false;
			} else {
				builder.setCharAt(index, Character.toLowerCase(character));
			}
		}

		return builder.toString();
	}

	/**
	 * Compresses the input text ({@code in}) and places the result in the {@code out} array.
	 *
	 * @param in The input text.
	 * @param out The output array.
	 * @return The number of bytes written to the output array.
	 */
	public static int compress(String in, byte[] out) {
		if (in.length() > 80) {
			in = in.substring(0, 80);
		}
		in = in.toLowerCase();

		int carry = -1;
		int outPos = 0;
		for (int inPos = 0; inPos < in.length(); inPos++) {
			char c = in.charAt(inPos);
			int tblPos = 0;
			for (int i = 0; i < FREQUENCY_ORDERED_CHARS.length; i++) {
				if (c == FREQUENCY_ORDERED_CHARS[i]) {
					tblPos = i;
					break;
				}
			}
			if (tblPos > 12) {
				tblPos += 195;
			}
			if (carry == -1) {
				if (tblPos < 13) {
					carry = tblPos;
				} else {
					out[outPos++] = (byte) tblPos;
				}
			} else if (tblPos < 13) {
				out[outPos++] = (byte) ((carry << 4) + tblPos);
				carry = -1;
			} else {
				out[outPos++] = (byte) ((carry << 4) + (tblPos >> 4));
				carry = tblPos & 0xF;
			}
		}
		if (carry != -1) {
			out[outPos++] = (byte) (carry << 4);
		}
		return outPos;
	}

	/**
	 * Filters invalid characters from the specified string.
	 *
	 * @param str The input string.
	 * @return The filtered string.
	 */
	public static String filterInvalidCharacters(String str) {
		StringBuilder builder = new StringBuilder();
		for (char c : str.toLowerCase().toCharArray()) {
			for (char validChar : FREQUENCY_ORDERED_CHARS) {
				if (c == validChar) {
					builder.append(c);
					break;
				}
			}
		}
		return builder.toString();
	}

	/**
	 * Uncompresses the compressed data ({@code in}) with the length ({@code len}) and returns the uncompressed
	 * {@link String}.
	 *
	 * @param in The compressed input data.
	 * @param len The length.
	 * @return The uncompressed {@link String}.
	 */
	public static String decompress(byte[] in, int len) {
		byte[] out = new byte[4096];
		int outPos = 0;
		int carry = -1;

		for (int i = 0; i < len * 2; i++) {
			int tblPos = in[i / 2] >> 4 - 4 * (i % 2) & 0xF;
			if (carry == -1) {
				if (tblPos < 13) {
					out[outPos++] = (byte) FREQUENCY_ORDERED_CHARS[tblPos];
				} else {
					carry = tblPos;
				}
			} else {
				out[outPos++] = (byte) FREQUENCY_ORDERED_CHARS[(carry << 4) + tblPos - 195];
				carry = -1;
			}
		}
		return new String(out, 0, outPos);
	}

	/** Formats digits for integers. */
	public static String formatDigits(final int amount) {
		return NumberFormat.getInstance().format(amount);
	}

	/** Formats digits for longs. */
	public static String formatDigits(final long amount) {
		return NumberFormat.getInstance().format(amount);
	}

	/** Formats digits for doubles. */
	public static String formatDigits(final double amount) {
		return NumberFormat.getInstance().format(amount);
	}

	/** Formats a price for longs. */
	public static String formatPrice(final long amount) {
		if (amount >= 0 && amount < 1_000)
			return "" + amount;
		if (amount >= 1_000 && amount < 1_000_000) {
			return (amount / 1_000) + "K";
		}
		if (amount >= 1_000_000 && amount < 1_000_000_000) {
			return (amount / 1_000_000) + "M";
		}
		if (amount >= 1_000_000_000 && amount < Integer.MAX_VALUE) {
			return (amount / 1_000_000_000) + "B";
		}
		return "<col=fc2a2a>Lots!";
	}

	/** Capitalize each letter after . */
	public static String capitalizeSentence(final String string) {
		int pos = 0;
		boolean capitalize = true;
		StringBuilder sb = new StringBuilder(string);
		while (pos < sb.length()) {
			if (sb.charAt(pos) == '.') {
				capitalize = true;
			} else if (capitalize && !Character.isWhitespace(sb.charAt(pos))) {
				sb.setCharAt(pos, Character.toUpperCase(sb.charAt(pos)));
				capitalize = false;
			}
			pos++;
		}
		return sb.toString();
	}

	public static String formatText(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (i == 0) {
				s = String.format("%s%s", Character.toUpperCase(s.charAt(0)), s.substring(1));
			}
			if (!Character.isLetterOrDigit(s.charAt(i))) {
				if (i + 1 < s.length()) {
					s = String.format("%s%s%s", s.subSequence(0, i + 1), Character.toUpperCase(s.charAt(i + 1)),
							s.substring(i + 2));
				}
			}
		}
		return s.replace("_", " ");
	}
}
