package net.edge.net.codec.game;

/**
 * An enumerated type whose elements represent the possible {@link IncomingMsg} types.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public enum GamePacketType {

	/**
	 * Represents a non-game packet of data.
	 */
	RAW,

	/**
	 * Represents a fixed length game packet.
	 */
	FIXED,

	/**
	 * Represents a variable byte length game packet.
	 */
	VARIABLE_BYTE,

	/**
	 * Represents a variable short length game packet.
	 */
	VARIABLE_SHORT
}