package net.edge.utils;

import net.edge.cache.FileSystem;

import java.util.stream.IntStream;

/**
 * The static-utility class that contains text utility functions.
 * @author lare96 <http://github.com/lare96>
 */
public final class TextUtils {
	
	/**
	 * The array of characters used for unpacking text.
	 */
	public static final char CHARACTER_TABLE[] = {' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%', '"', '[', ']'};
	
	/**
	 * The array of valid characters.
	 */
	public static final char VALID_CHARACTERS[] = {'_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+', '=', ':', ';', '.', '>', '<', ',', '"', '[', ']', '|', '?', '/', '`'};
	
	/**
	 * The default constructor.
	 * @throws UnsupportedOperationException if this class is instantiated.
	 */
	private TextUtils() {
		throw new UnsupportedOperationException("This class cannot be " + "instantiated!");
	}
	
	/**
	 * Determines the indefinite article of {@code thing}.
	 * @param thing the thing to determine for.
	 * @return the indefinite article.
	 */
	public static String determineIndefiniteArticle(String thing) {
		char first = thing.toLowerCase().charAt(0);
		boolean vowel = first == 'a' || first == 'e' || first == 'i' || first == 'o' || first == 'u';
		return vowel ? "an" : "a";
	}
	
	/**
	 * Determines the plural check of {@code thing}.
	 * @param thing the thing to determine for.
	 * @return the plural check.
	 */
	public static String determinePluralCheck(String thing) {
		boolean needsPlural = !thing.endsWith("s") && !thing.endsWith(")");
		return needsPlural ? "s" : "";
	}
	
	/**
	 * Appends the determined plural check to {@code thing}.
	 * @param thing the thing to append.
	 * @return the {@code thing} after the plural check has been appended.
	 */
	public static String appendPluralCheck(String thing) {
		return thing.concat(determinePluralCheck(thing));
	}
	
	/**
	 * Appends the determined indefinite article to {@code thing}.
	 * @param thing the thing to append.
	 * @return the {@code thing} after the indefinite article has been appended.
	 */
	public static String appendIndefiniteArticle(String thing) {
		return determineIndefiniteArticle(thing).concat(" " + thing);
	}
	
	/**
	 * Appends the determined indefinite article to {@code thing}.
	 * @param thing the thing to append.
	 * @return the {@code thing} after the indefinite article has been appended.
	 */
	public static String appendIndefiniteArticleNoVowel(String thing) {
		return " " + thing;
	}
	
	/**
	 * Capitalizes the first character of {@code str}. Any leading or trailing
	 * whitespace in the string should be trimmed before using this method.
	 * @param str the string to capitalize.
	 * @return the capitalized string.
	 */
	public static String capitalize(String str) {
		return str.substring(0, 1).toUpperCase().concat(str.substring(1, str.length()));
	}
	
	/**
	 * Formats {@code price} into K, million, or its default value.
	 * @param price the price to format.
	 * @return the newly formatted price.
	 */
	public static String formatPrice(int price) {
		if(price >= 1000 && price < 1000000) {
			return "(" + (price / 1000) + "K)";
		} else if(price >= 1000000) {
			return "(" + (price / 1000000) + " million)";
		}
		return Integer.toString(price);
	}
	
	/**
	 * Checks the complexity of the password
	 * @param pass the password string to check.
	 * @return the complexity of the password in a string.
	 */
	public static String passwordCheck(String pass) {
		boolean containsUpperCase = false;
		boolean containsLowerCase = false;
		boolean containsDigit = false;
		for(char ch : pass.toCharArray()) {
			if(Character.isUpperCase(ch))
				containsUpperCase = true;
			if(Character.isLowerCase(ch))
				containsLowerCase = true;
			if(Character.isDigit(ch))
				containsDigit = true;
		}
		if(containsDigit && containsLowerCase && containsUpperCase) {
			return "@gre@strong";
		} else {
			if(containsLowerCase && (containsDigit || containsUpperCase)) {
				return "@yel@good";
			} else {
				return "@red@weak";
			}
		}
	}
	
	/**
	 * Converts a {@code long} hash into a string value.
	 * @param l the long to convert.
	 * @return the converted string.
	 */
	public static String hashToName(long l) {
		int i = 0;
		char ac[] = new char[12];
		while(l != 0L) {
			long l1 = l;
			l /= 37L;
			ac[11 - i++] = VALID_CHARACTERS[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}
	
	/**
	 * Converts a string to a {@code long} hash value.
	 * @param s the string to convert.
	 * @return the long hash value.
	 */
	public static long nameToHash(String s) {
		long l = 0L;
		for(int i = 0; i < s.length() && i <= 12; i++) {
			char c = s.charAt(i);
			l *= 37L;
			if(c >= 'A' && c <= 'Z')
				l += (1 + c) - 65;
			else if(c >= 'a' && c <= 'z')
				l += (1 + c) - 97;
			else if(c >= '0' && c <= '9')
				l += (27 + c) - 48;
		}
		while(l % 37L == 0L && l != 0L)
			l /= 37L;
		return l;
	}
	
	/**
	 * Hashes a {@code String} using Jagex's algorithm, this method should be
	 * used to convert actual names to hashed names to lookup files within the
	 * {@link FileSystem}.
	 * @param string The string to hash.
	 * @return The hashed string.
	 */
	public static int hash(String string) {
		return _hash(string.toUpperCase());
	}
	
	/**
	 * Hashes a {@code String} using Jagex's algorithm, this method should be
	 * used to convert actual names to hashed names to lookup files within the
	 * {@link FileSystem}.
	 * <p>
	 * <p>
	 * This method should <i>only</i> be used internally, it is marked
	 * deprecated as it does not properly hash the specified {@code String}. The
	 * functionality of this method is used to register a proper {@code String}
	 * {@link #hash(String) <i>hashing method</i>}. The scope of this method has
	 * been marked as {@code private} to prevent confusion.
	 * </p>
	 * @param string The string to hash.
	 * @return The hashed string.
	 * @deprecated This method should only be used internally as it does not
	 * correctly hash the specified {@code String}. See the note
	 * below for more information.
	 */
	@Deprecated
	private static int _hash(String string) {
		return IntStream.range(0, string.length()).reduce(0, (hash, index) -> hash * 61 + string.charAt(index) - 32);
	}
}
