package com.rageps.net.discord.cmd;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import java.util.Arrays;

/**
 * Represents the arguments of a {@link CommandListener}.
 */
public final class CommandArguments {

	/**
	 * The arguments.
	 */
	private final String[] arguments;

	/**
	 * The current argument index.
	 */
	private int index;

	/**
	 * Constructs a new {@link CommandArguments} with the specified arguments.
	 *
	 * @param arguments The CommandListener's arguments.
	 */
	public CommandArguments(String[] arguments) {
		this.arguments = arguments.clone();
	}

	/**
	 * Gets the remaining amount of arguments.
	 *
	 * @return The amount of remaining arguments.
	 */
	public int remaining() {
		return arguments.length - index;
	}

	/**
	 * Tests whether or not there is at least {@code amount} remaining arguments.
	 *
	 * @param amount The amount of arguments.
	 * @return {@code true} if there is at least {@code amount} arguments.
	 */
	public boolean hasRemaining(int amount) {
		return remaining() >= amount;
	}

	/**
	 * Tests whether or not there is at least one remaining argument.
	 *
	 * @return {@code true} if there is at least 1 remaining argument.
	 */
	public boolean hasRemaining() {
		return hasRemaining(1);
	}

	/**
	 * Rolls back the index by <tt>1</tt> if it is greater than <tt>0</tt>.
	 */
	public void rollback() {
		if (index > 0) {
			index--;
		}
	}

	/**
	 * Gets all of the arguments delimited by the specified {@code delim}.
	 *
	 * @param delim The delimiter.
	 * @return All of the arguments imploded into a String, delimited by {@code delim}.
	 */
	public String get(char delim) {
		StringBuilder builder = new StringBuilder(arguments.length * 16);
		for (String argument : arguments) {
			builder.append(argument).append(delim);
		}
		return builder.toString();
	}

	/**
	 * Gets all of the remaining arguments delimited by the specified {@code delim}.
	 *
	 * @param delim The delimiter.
	 * @return All of the remaining arguments imploded into a String, delimited by {@code delim}.
	 */
	public String getRemaining(char delim) {
		StringBuilder builder = new StringBuilder(arguments.length * 16);
		for (int start = index; start < arguments.length; start++) {
			builder.append(arguments[start]).append(delim);
		}
		return builder.toString();
	}

	/**
	 * Gets all of the arguments as a single String, delimited by a space character.
	 *
	 * @return All of the arguments imploded into a String, delimited by a space character.
	 */
	public String get() {
		return get(' ');
	}

	/**
	 * Gets all of the remaining arguments as a single String, delimited by a space character.
	 *
	 * @return All of the remaining arguments imploded into a String, delimited by a space character.
	 */
	public String getRemaining() {
		return getRemaining(' ');
	}

	/**
	 * Gets the next argument.
	 *
	 * @return The next argument.
	 */
	public String getNext() {
		return getString(index++);
	}

	/**
	 * Gets the next argument as an {@code int}.
	 *
	 * @return The next argument as an {@code int}.
	 */
	public int getNextInteger() {
		return getInteger(index++);
	}

	/**
	 * Gets the next argument as a {@code boolean}.
	 *
	 * @return The next argument as a {@code boolean}.
	 */
	public boolean getNextBoolean() {
		return getBoolean(index++);
	}

	/**
	 * Gets the next argument as a {@code long}.
	 *
	 * @return The next argument as a {@code long}.
	 */
	public long getNextLong() {
		return getLong(index++);
	}

	/**
	 * Gets the next argument as a {@code double}.
	 *
	 * @return The next argument as a {@code double}.
	 */
	public double getNextDouble() {
		return getDouble(index++);
	}

	/**
	 * Attempts to get the argument at the specified index.
	 *
	 * @param index The index of the argument.
	 * @return The argument at {@code index}.
	 */
	public String getString(int index) {
		return checkIndex(index);
	}

	/**
	 * Attempts to get an {@code int} at the specified index.
	 *
	 * @param index The index of the argument to parse an int.
	 * @return The argument at {@code index} represented as an int.
	 */
	public int getInteger(int index) {
		String argument = checkIndex(index);
		try {
			return Integer.parseInt(argument);
		} catch (NumberFormatException cause) {
			throw new NumberFormatException("Argument: " + argument + " at index: " + index + " cannot be parsed as an integer.");
		}
	}

	/**
	 * Attempts to get a {@code boolean} at the specified index.
	 *
	 * @param index The index of the argument to parse a boolean.
	 * @return The argument at {@code index} represented as a boolean.
	 */
	public boolean getBoolean(int index) {
		String argument = checkIndex(index);
		if (!argument.equalsIgnoreCase("true") && !argument.equalsIgnoreCase("false")) {
			throw new IllegalArgumentException("Only \"true\" or \"false\" is accepted for boolean input.");
		}
		return Boolean.parseBoolean(argument);
	}

	/**
	 * Attempts to get a {@code long} at the specified index.
	 *
	 * @param index The index of the argument to parse a long.
	 * @return The argument at {@code index} represented as a long.
	 */
	public long getLong(int index) {
		String argument = checkIndex(index);
		try {
			return Long.parseLong(argument);
		} catch (NumberFormatException cause) {
			throw new NumberFormatException("Argument: " + argument + " at index: " + index + " cannot be parsed as a long.");
		}
	}

	/**
	 * Attempts to get a {@code double} at the specified index.
	 *
	 * @param index The index of the argument to parse a double.
	 * @return The argument at {@code index} represented as a double.
	 */
	public double getDouble(int index) {
		String argument = checkIndex(index);
		try {
			return Double.parseDouble(argument);
		} catch (NumberFormatException cause) {
			throw new NumberFormatException("Argument: " + argument + " at index: " + index + " cannot be parsed as a double.");
		}
	}

	/**
	 * Ensures the specified index is within bounds and is non-null.
	 *
	 * @param index The current argument index.
	 * @return The argument at {@code index}.
	 */
	private String checkIndex(int index) {
		Preconditions.checkElementIndex(index, arguments.length, "Index out of bounds: " + index + " length: " + arguments.length);
		return Preconditions.checkNotNull(arguments[index], "Argument for index: " + index + " is null.");
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("arguments", Arrays.toString(arguments)).toString();
	}
	
	public int getSize() {
		return arguments.length;
	}

}
