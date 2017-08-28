package net.edge.net.codec;

/**
 * An enumerated type whose elements represent the possible custom Runescape value types.
 *
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public enum ByteTransform {
	/**
	 * Do nothing to the value.
	 */
	NORMAL,

	/**
	 * Add {@code 128} to the value.
	 */
	A,

	/**
	 * Invert the sign of the value.
	 */
	C,

	/**
	 * Subtract {@code 128} from the value.
	 */
	S
}