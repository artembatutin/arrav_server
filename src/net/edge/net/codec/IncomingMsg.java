package net.edge.net.codec;

import io.netty.buffer.ByteBuf;
import net.edge.net.packet.PacketUtils;

/**
 * A {@link ByteBuf} wrapper tailored to the specifications of the Runescape protocol. These wrappers are backed by pooled
 * direct buffers when possible, otherwise they're backed by pooled heap buffers.
 *
 * @author lare96 <http://github.org/lare96>
 */
public final class IncomingMsg {
	/**
	 * The backing byte buffer used to read and write data.
	 */
	private final ByteBuf buf;

	/**
	 * The opcode, {@code -1} if this message does not have an opcode.
	 */
	private final int opcode;

	/**
	 * The header type of this message.
	 */
	private final PacketType type;

	/**
	 * Creates a new {@link IncomingMsg}.
	 */
	public IncomingMsg(int opcode, PacketType type, ByteBuf buf) {
		this.buf = buf;
		this.opcode = opcode;
		this.type = type;
	}

	/**
	 * Reads a value as a {@code byte}.
	 *
	 * @param signed if the byte is signed.
	 * @param type   The byte transformation type
	 * @return The value of the byte.
	 */
	public int get(boolean signed, ByteTransform type) {
		int value = buf.readByte();
		switch(type) {
			case A:
				value = value - 128;
				break;
			case C:
				value = -value;
				break;
			case S:
				value = 128 - value;
				break;
			case NORMAL:
				break;
		}
		return signed ? value : value & 0xff;
	}

	/**
	 * Reads a standard signed {@code byte}.
	 *
	 * @return The value of the byte.
	 */
	public int get() {
		return get(true, ByteTransform.NORMAL);
	}

	/**
	 * Reads a standard {@code byte}.
	 *
	 * @param signed If the byte is signed.
	 * @return The value of the byte.
	 */
	public int get(boolean signed) {
		return get(signed, ByteTransform.NORMAL);
	}

	/**
	 * Reads a signed {@code byte}.
	 *
	 * @param type The byte transformation type
	 * @return The value of the byte.
	 */
	public int get(ByteTransform type) {
		return get(true, type);
	}

	/**
	 * Reads a {@code medium} / {@code tri-byte} value.
	 *
	 * @return the value of the medium.
	 */
	public int getMedium() {
		int value = 0;
		value |= get(false) << 16;
		value |= get(false) << 8;
		value |= get(false);
		return value & 0xffffff;
	}

	/**
	 * Reads a {@code short} value.
	 *
	 * @param signed If the short is signed.
	 * @param type   The byte transformation type
	 * @param order  The byte endianness type.
	 * @return The value of the short.
	 * @throws UnsupportedOperationException if middle or inverse-middle value types are selected.
	 */
	public int getShort(boolean signed, ByteTransform type, ByteOrder order) {
		int value = 0;
		switch(order) {
			case BIG:
				value |= get(false) << 8;
				value |= get(false, type);
				break;
			case MIDDLE:
				throw new UnsupportedOperationException("Middle-endian short is impossible!");
			case INVERSE_MIDDLE:
				throw new UnsupportedOperationException("Inverse-middle-endian short is impossible!");
			case LITTLE:
				value |= get(false, type);
				value |= get(false) << 8;
				break;
		}
		return signed ? value : value & 0xffff;
	}

	/**
	 * Reads a standard signed big-endian {@code short}.
	 *
	 * @return The value of the short.
	 */
	public int getShort() {
		return getShort(true, ByteTransform.NORMAL, ByteOrder.BIG);
	}

	/**
	 * Reads a standard big-endian {@code short}.
	 *
	 * @param signed If the short is signed.
	 * @return The value of the short.
	 */
	public int getShort(boolean signed) {
		return getShort(signed, ByteTransform.NORMAL, ByteOrder.BIG);
	}

	/**
	 * Reads a signed big-endian {@code short}.
	 *
	 * @param type The byte transformation type
	 * @return The value of the short.
	 */
	public int getShort(ByteTransform type) {
		return getShort(true, type, ByteOrder.BIG);
	}

	/**
	 * Reads a big-endian {@code short}.
	 *
	 * @param signed If the short is signed.
	 * @param type   The byte transformation type
	 * @return The value of the short.
	 */
	public int getShort(boolean signed, ByteTransform type) {
		return getShort(signed, type, ByteOrder.BIG);
	}

	/**
	 * Reads a signed standard {@code short}.
	 *
	 * @param order The byte endianness type.
	 * @return The value of the short.
	 */
	public int getShort(ByteOrder order) {
		return getShort(true, ByteTransform.NORMAL, order);
	}

	/**
	 * Reads a standard {@code short}.
	 *
	 * @param signed If the short is signed.
	 * @param order  The byte endianness type.
	 * @return The value of the short.
	 */
	public int getShort(boolean signed, ByteOrder order) {
		return getShort(signed, ByteTransform.NORMAL, order);
	}

	/**
	 * Reads a signed {@code short}.
	 *
	 * @param type  The byte transformation type
	 * @param order The byte endianness type.
	 * @return The value of the short.
	 */
	public int getShort(ByteTransform type, ByteOrder order) {
		return getShort(true, type, order);
	}

	/**
	 * Reads an {@code int}.
	 *
	 * @param signed If the integer is signed.
	 * @param type   The byte transformation type
	 * @param order  The byte endianness type.
	 * @return The value of the integer.
	 */
	public int getInt(boolean signed, ByteTransform type, ByteOrder order) {
		long value = 0;
		switch(order) {
			case BIG:
				value |= get(false) << 24;
				value |= get(false) << 16;
				value |= get(false) << 8;
				value |= get(false, type);
				break;
			case MIDDLE:
				value |= get(false) << 8;
				value |= get(false, type);
				value |= get(false) << 24;
				value |= get(false) << 16;
				break;
			case INVERSE_MIDDLE:
				value |= get(false) << 16;
				value |= get(false) << 24;
				value |= get(false, type);
				value |= get(false) << 8;
				break;
			case LITTLE:
				value |= get(false, type);
				value |= get(false) << 8;
				value |= get(false) << 16;
				value |= get(false) << 24;
				break;
		}
		return (int) (signed ? value : value & 0xffffffffL);
	}

	/**
	 * Reads a signed standard big-endian {@code int}.
	 *
	 * @return The value of the integer.
	 */
	public int getInt() {
		return getInt(true, ByteTransform.NORMAL, ByteOrder.BIG);
	}

	/**
	 * Reads a standard big-endian {@code int}.
	 *
	 * @param signed If the integer is signed.
	 * @return The value of the integer.
	 */
	public int getInt(boolean signed) {
		return getInt(signed, ByteTransform.NORMAL, ByteOrder.BIG);
	}

	/**
	 * Reads a signed big-endian {@code int}.
	 *
	 * @param type The byte transformation type
	 * @return The value of the integer.
	 */
	public int getInt(ByteTransform type) {
		return getInt(true, type, ByteOrder.BIG);
	}

	/**
	 * Reads a big-endian {@code int}.
	 *
	 * @param signed If the integer is signed.
	 * @param type   The byte transformation type
	 * @return The value of the integer.
	 */
	public int getInt(boolean signed, ByteTransform type) {
		return getInt(signed, type, ByteOrder.BIG);
	}

	/**
	 * Reads a signed standard {@code int}.
	 *
	 * @param order The byte endianness type.
	 * @return The value of the integer.
	 */
	public int getInt(ByteOrder order) {
		return getInt(true, ByteTransform.NORMAL, order);
	}

	/**
	 * Reads a standard {@code int}.
	 *
	 * @param signed If the integer is signed.
	 * @param order  The byte endianness type.
	 * @return The value of the integer.
	 */
	public int getInt(boolean signed, ByteOrder order) {
		return getInt(signed, ByteTransform.NORMAL, order);
	}

	/**
	 * Reads a signed {@code int}.
	 *
	 * @param type  The byte transformation type
	 * @param order The byte endianness type.
	 * @return The value of the integer.
	 */
	public int getInt(ByteTransform type, ByteOrder order) {
		return getInt(true, type, order);
	}

	/**
	 * Reads a signed {@code long} value.
	 *
	 * @param type  The byte transformation type
	 * @param order The byte endianness type.
	 * @return The value of the long.
	 * @throws UnsupportedOperationException if middle or inverse-middle value types are selected.
	 */
	public long getLong(ByteTransform type, ByteOrder order) {
		long value = 0;
		switch(order) {
			case BIG:
				value |= (long) get(false) << 56L;
				value |= (long) get(false) << 48L;
				value |= (long) get(false) << 40L;
				value |= (long) get(false) << 32L;
				value |= (long) get(false) << 24L;
				value |= (long) get(false) << 16L;
				value |= (long) get(false) << 8L;
				value |= get(false, type);
				break;
			case INVERSE_MIDDLE:
			case MIDDLE:
				throw new UnsupportedOperationException("Middle and inverse-middle value types not supported!");
			case LITTLE:
				value |= get(false, type);
				value |= (long) get(false) << 8L;
				value |= (long) get(false) << 16L;
				value |= (long) get(false) << 24L;
				value |= (long) get(false) << 32L;
				value |= (long) get(false) << 40L;
				value |= (long) get(false) << 48L;
				value |= (long) get(false) << 56L;
				break;
		}
		return value;
	}

	/**
	 * Reads a signed standard big-endian {@code long}.
	 *
	 * @return The value of the long.
	 */
	public long getLong() {
		return getLong(ByteTransform.NORMAL, ByteOrder.BIG);
	}

	/**
	 * Reads a signed big-endian {@code long}.
	 *
	 * @param type The byte transformation type.
	 * @return The value of the long.
	 */
	public long getLong(ByteTransform type) {
		return getLong(type, ByteOrder.BIG);
	}

	/**
	 * Reads a signed standard {@code long}.
	 *
	 * @param order The byte endianness type.
	 * @return The value of the long.
	 */
	public long getLong(ByteOrder order) {
		return getLong(ByteTransform.NORMAL, order);
	}

	/**
	 * Reads a RuneScape {@code String} value.
	 *
	 * @return The value of the string.
	 */
	public String getCString() { // very weird
		return PacketUtils.getCString(buf);
	}

	/**
	 * Reads the amount of bytes into the array, starting at the current position.
	 *
	 * @param amount The amount to read.
	 * @return A buffer filled with the data.
	 */
	public byte[] getBytes(int amount) {
		return getBytes(amount, ByteTransform.NORMAL);
	}

	/**
	 * Reads the amount of bytes into a byte array, starting at the current position.
	 *
	 * @param amount The amount of bytes.
	 * @param type   The byte transformation type of each byte.
	 * @return A buffer filled with the data.
	 */
	public byte[] getBytes(int amount, ByteTransform type) {
		byte[] data = new byte[amount];
		for(int i = 0; i < amount; i++) {
			data[i] = (byte) get(type);
		}
		return data;
	}

	/**
	 * Reads the amount of bytes from the buffer in reverse, starting at {@code current_position + amount} and reading in
	 * reverse until the current position.
	 *
	 * @param amount The amount of bytes to read.
	 * @param type   The byte transformation type of each byte.
	 * @return A buffer filled with the data.
	 */
	public byte[] getBytesReverse(int amount, ByteTransform type) {
		byte[] data = new byte[amount];
		int dataPosition = 0;
		for(int i = buf.readerIndex() + amount - 1; i >= buf.readerIndex(); i--) {
			int value = buf.getByte(i);
			switch(type) {
				case A:
					value -= 128;
					break;
				case C:
					value = -value;
					break;
				case S:
					value = 128 - value;
					break;
				case NORMAL:
					break;
			}
			data[dataPosition++] = (byte) value;
		}
		return data;
	}

	/**
	 * @return The backing byte buffer.
	 */
	public ByteBuf getBuffer() {
		return buf;
	}

	/**
	 * @return The opcode, {@code -1} if this message does not have an opcode.
	 */
	public int getOpcode() {
		return opcode;
	}

	/**
	 * @return The size of the payload.
	 */
	public int getSize() {
		return buf.capacity();
	}

	/**
	 * @return The header type of this message.
	 */
	public PacketType getType() {
		return type;
	}
}