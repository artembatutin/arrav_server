package net.edge.world.model.node.entity.player.assets;

import java.util.Arrays;

/**
 * The enumerated type whose elements represent the types of authority a player
 * can have.
 * @author lare96 <http://github.com/lare96>
 */
public enum Rights {
	PLAYER(0, 0, ""),
	DESIGNER(5, 1, "@blu@"),
	RESPECTED_MEMBER(6, 2, "@blu@"),
	DONATOR(7, 3, "@red@"),
	SUPER_DONATOR(8, 4, "@red@"),
	EXTREME_DONATOR(9, 5, "@gre@"),
	MODERATOR(1, 6, "@or1@"),
	SUPER_MODERATOR(2, 7, "@or2@"),
	ADMINISTRATOR(3, 8, "@or3@"),
	DEVELOPER(4, 9, "@or3@");
	
	/**
	 * The value of this rank as seen by the protocol.
	 */
	private final int protocolValue;
	
	/**
	 * The value of this rank as seen by the server. This value will be used to
	 * determine which of the elements are greater than each other.
	 */
	private final int value;
	
	/**
	 * The yell-prefix for this right.
	 */
	private final String yell;
	
	/**
	 * Create a new {@link Rights}.
	 * @param protocolValue the value of this rank as seen by the protocol.
	 * @param value         the value of this rank as seen by the server.
	 * @param yell          {@link #yell}.
	 */
	Rights(int protocolValue, int value, String yell) {
		this.protocolValue = protocolValue;
		this.value = value;
		this.yell = yell;
	}
	
	/**
	 * Determines if this right is greater than the argued right. Please note
	 * that this method <b>does not</b> compare the Objects themselves, but
	 * instead compares the value behind them as specified by {@code value} in
	 * the enumerated type.
	 * @param other the argued right to compare.
	 * @return {@code true} if this right is greater, {@code false} otherwise.
	 */
	public final boolean greater(Rights other) {
		return value > other.value;
	}
	
	/**
	 * Determines if this right is lesser than the argued right. Please note
	 * that this method <b>does not</b> compare the Objects themselves, but
	 * instead compares the value behind them as specified by {@code value} in
	 * the enumerated type.
	 * @param other the argued right to compare.
	 * @return {@code true} if this right is lesser, {@code false} otherwise.
	 */
	public final boolean less(Rights other) {
		return value < other.value;
	}
	
	/**
	 * Determines if this right is equal in power to the argued right. Please
	 * note that this method <b>does not</b> compare the Objects themselves, but
	 * instead compares the value behind them as specified by {@code value} in
	 * the enumerated type.
	 * @param other the argued right to compare.
	 * @return {@code true} if this right is equal, {@code false} otherwise.
	 */
	public final boolean equal(Rights other) {
		return value == other.value;
	}
	
	/**
	 * Gets the value of this rank as seen by the protocol.
	 * @return the protocol value of this rank.
	 */
	public final int getProtocolValue() {
		return protocolValue;
	}
	
	public final boolean equals(Rights... rights) {
		return Arrays.stream(rights).anyMatch(right -> this == right);
	}
	
	public final boolean isStaff() {
		return this.equals(MODERATOR) || this.equals(SUPER_MODERATOR) || this.equals(ADMINISTRATOR) || this.equals(DEVELOPER);
	}
	
	public final boolean isDonator() {
		return this.equals(DONATOR) || this.equals(SUPER_DONATOR) || this.equals(EXTREME_DONATOR);
	}
	
	/**
	 * Gets the value of this rank as seen by the server.
	 * @return the server value of this rank.
	 */
	public final int getValue() {
		return value;
	}
	
	/**
	 * Gets the yell prefix of this rank as seen by the server.
	 * @return the yell prefix of this rank.
	 */
	public final String getYellPrefix() {
		return yell;
	}
	
	@Override
	public final String toString() {
		return name().replaceAll("_", " ").toLowerCase();
	}
}
