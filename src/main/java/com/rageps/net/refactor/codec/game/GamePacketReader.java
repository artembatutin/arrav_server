package com.rageps.net.refactor.codec.game;

import com.google.common.base.Preconditions;
import com.rageps.util.BufferUtil;
import io.netty.buffer.ByteBuf;


/**
 * A utility class for reading {@link GamePacket}s.
 *
 * @author Graham
 */
public final class GamePacketReader {

	/**
	 * The current bit index.
	 */
	private int bitIndex;

	/**
	 * The buffer.
	 */
	private final ByteBuf buffer;

	/**
	 * The current mode.
	 */
	private AccessMode mode = AccessMode.BYTE_ACCESS;

	/**
	 * Creates the reader.
	 *
	 * @param packet The packet.
	 */
	public GamePacketReader(GamePacket packet) {
		buffer = packet.content();
	}

	/**
	 * Checks that this reader is in the bit access mode.
	 *
	 * @throws IllegalStateException If the reader is not in bit access mode.
	 */
	private void checkBitAccess() {
		Preconditions.checkState(mode == AccessMode.BIT_ACCESS,
				"For bit-based calls to work, the mode must be bit access.");
	}

	/**
	 * Checks that this reader is in the byte access mode.
	 *
	 * @throws IllegalStateException If the reader is not in byte access mode.
	 */
	private void checkByteAccess() {
		Preconditions.checkState(mode == AccessMode.BYTE_ACCESS,
				"For byte-based calls to work, the mode must be byte access.");
	}

	/**
	 * Reads a standard data type from the buffer with the specified order and transformation.
	 *
	 * @param type The data type.
	 * @param order The data order.
	 * @param transformation The data transformation.
	 * @return The value.
	 * @throws IllegalStateException If this reader is not in byte access mode.
	 * @throws IllegalArgumentException If the combination is invalid.
	 */
	private long get(DataType type, DataOrder order, DataTransformation transformation) {
		checkByteAccess();
		long longValue = 0;
		int length = type.getBytes();
		if (order == DataOrder.BIG) {
			for (int i = length - 1; i >= 0; i--) {
				if (i == 0 && transformation != DataTransformation.NONE) {
					if (transformation == DataTransformation.ADD) {
						longValue |= buffer.readByte() - 128 & 0xFFL;
					} else if (transformation == DataTransformation.NEGATE) {
						longValue |= -buffer.readByte() & 0xFFL;
					} else if (transformation == DataTransformation.SUBTRACT) {
						longValue |= 128 - buffer.readByte() & 0xFFL;
					} else {
						throw new IllegalArgumentException("Unknown transformation.");
					}
				} else {
					longValue |= (buffer.readByte() & 0xFFL) << i * 8;
				}
			}
		} else if (order == DataOrder.LITTLE) {
			for (int i = 0; i < length; i++) {
				if (i == 0 && transformation != DataTransformation.NONE) {
					if (transformation == DataTransformation.ADD) {
						longValue |= buffer.readByte() - 128 & 0xFFL;
					} else if (transformation == DataTransformation.NEGATE) {
						longValue |= -buffer.readByte() & 0xFFL;
					} else if (transformation == DataTransformation.SUBTRACT) {
						longValue |= 128 - buffer.readByte() & 0xFFL;
					} else {
						throw new IllegalArgumentException("Unknown transformation.");
					}
				} else {
					longValue |= (buffer.readByte() & 0xFFL) << i * 8;
				}
			}
		} else if (order == DataOrder.MIDDLE) {
			if (transformation != DataTransformation.NONE) {
				throw new IllegalArgumentException("Middle endian cannot be transformed.");
			}
			if (type != DataType.INT) {
				throw new IllegalArgumentException("Middle endian can only be used with an integer.");
			}
			longValue |= (buffer.readByte() & 0xFF) << 8;
			longValue |= buffer.readByte() & 0xFF;
			longValue |= (buffer.readByte() & 0xFF) << 24;
			longValue |= (buffer.readByte() & 0xFF) << 16;
		} else if (order == DataOrder.INVERSED_MIDDLE) {
			if (transformation != DataTransformation.NONE) {
				throw new IllegalArgumentException("Inversed middle endian cannot be transformed.");
			}
			if (type != DataType.INT) {
				throw new IllegalArgumentException("Inversed middle endian can only be used with an integer.");
			}
			longValue |= (buffer.readByte() & 0xFF) << 16;
			longValue |= (buffer.readByte() & 0xFF) << 24;
			longValue |= buffer.readByte() & 0xFF;
			longValue |= (buffer.readByte() & 0xFF) << 8;
		} else {
			throw new IllegalArgumentException("Unknown order.");
		}
		return longValue;
	}

	/**
	 * Gets a bit from the buffer.
	 *
	 * @return The value.
	 * @throws IllegalStateException If the reader is not in bit access mode.
	 */
	public int getBit() {
		return getBits(1);
	}

	/**
	 * Gets the specified amount of bits from the buffer.
	 *
	 * @param amount The amount of bits.
	 * @return The value.
	 * @throws IllegalStateException If the reader is not in bit access mode.
	 * @throws IllegalArgumentException If the number of bits is not between 1 and 31 inclusive.
	 */
	public int getBits(int amount) {
		Preconditions.checkArgument(amount >= 0 && amount <= 32, "Number of bits must be between 1 and 32 inclusive.");
		checkBitAccess();

		int bytePos = bitIndex >> 3;
		int bitOffset = 8 - (bitIndex & 7);
		int value = 0;
		bitIndex += amount;

		for (; amount > bitOffset; bitOffset = 8) {
			value += (buffer.getByte(bytePos++) & DataConstants.BIT_MASK[bitOffset]) << amount - bitOffset;
			amount -= bitOffset;
		}
		if (amount == bitOffset) {
			value += buffer.getByte(bytePos) & DataConstants.BIT_MASK[bitOffset];
		} else {
			value += buffer.getByte(bytePos) >> bitOffset - amount & DataConstants.BIT_MASK[amount];
		}
		return value;
	}

	/**
	 * Gets bytes.
	 *
	 * @param bytes The target byte array.
	 * @throws IllegalStateException If this reader is not in byte access mode.
	 */
	public void getBytes(byte[] bytes) {
		checkByteAccess();
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = buffer.readByte();
		}
	}

	/**
	 * Reads the amount of bytes into the array, starting at the current position.
	 * @param amount The amount to read.
	 * @return A buffer filled with the data.
	 */
	public byte[] getBytes(int amount) {
		return getBytes(amount, DataTransformation.NONE);
	}

	/**
	 * Reads the amount of bytes into a byte array, starting at the current position.
	 * @param amount The amount of bytes.
	 * @param type The byte transformation type of each byte.
	 * @return A buffer filled with the data.
	 */
	public byte[] getBytes(int amount, DataTransformation type) {
		byte[] data = new byte[amount];
		for(int i = 0; i < amount; i++) {
			data[i] = (byte) get(type);
		}
		return data;
	}


	/**
	 * Gets bytes with the specified transformation.
	 *
	 * @param transformation The transformation.
	 * @param bytes The target byte array.
	 * @throws IllegalStateException If this reader is not in byte access mode.
	 */
	public void getBytes(DataTransformation transformation, byte[] bytes) {
		if (transformation == DataTransformation.NONE) {
			getBytesReverse(bytes);
		} else {
			for (int i = 0; i < bytes.length; i++) {
				bytes[i] = (byte) getSigned(DataType.BYTE, transformation);
			}
		}
	}

	/**
	 * Gets bytes in reverse.
	 *
	 * @param bytes The target byte array.
	 * @throws IllegalStateException If this reader is not in byte access mode.
	 */
	public void getBytesReverse(byte[] bytes) {
		checkByteAccess();
		for (int i = bytes.length - 1; i >= 0; i--) {
			bytes[i] = buffer.readByte();
		}
	}

	/**
	 * Gets bytes in reverse with the specified transformation.
	 *
	 * @param transformation The transformation.
	 * @param bytes The target byte array.
	 * @throws IllegalStateException If this reader is not in byte access mode.
	 */
	public void getBytesReverse(DataTransformation transformation, byte[] bytes) {
		if (transformation == DataTransformation.NONE) {
			getBytesReverse(bytes);
		} else {
			for (int i = bytes.length - 1; i >= 0; i--) {
				bytes[i] = (byte) getSigned(DataType.BYTE, transformation);
			}
		}
	}

	/**
	 * Reads the amount of bytes from the buffer in reverse, starting at {@code current_position + amount} and reading in
	 * reverse until the current position.
	 * @param amount The amount of bytes to read.
	 * @param type The byte transformation type of each byte.
	 * @return A buffer filled with the data.
	 */
	public byte[] getBytesReverse(int amount, DataTransformation type) {
		byte[] data = new byte[amount];
		int dataPosition = 0;
		for(int i = buffer.readerIndex() + amount - 1; i >= buffer.readerIndex(); i--) {
			int value = buffer.getByte(i);
			switch(type) {
				case ADD:
					value -= 128;
					break;
				case NEGATE:
					value = -value;
					break;
				case SUBTRACT:
					value = 128 - value;
					break;
				case NONE:
					break;
			}
			data[dataPosition++] = (byte) value;
		}
		return data;
	}

	/**
	 * Gets the length of this reader.
	 *
	 * @return The length of this reader.
	 */
	public int getLength() {
		checkByteAccess();
		return buffer.writableBytes();
	}

	/**
	 * Gets a signed data type from the buffer.
	 *
	 * @param type The data type.
	 * @return The value.
	 * @throws IllegalStateException If this reader is not in byte access mode.
	 */
	public long getSigned(DataType type) {
		return getSigned(type, DataOrder.BIG, DataTransformation.NONE);
	}

	/**
	 * Gets a signed data type from the buffer with the specified order.
	 *
	 * @param type The data type.
	 * @param order The byte order.
	 * @return The value.
	 * @throws IllegalStateException If this reader is not in byte access mode.
	 * @throws IllegalArgumentException If the combination is invalid.
	 */
	public long getSigned(DataType type, DataOrder order) {
		return getSigned(type, order, DataTransformation.NONE);
	}

	/**
	 * Gets a signed data type from the buffer with the specified order and transformation.
	 *
	 * @param type The data type.
	 * @param order The byte order.
	 * @param transformation The data transformation.
	 * @return The value.
	 * @throws IllegalStateException If this reader is not in byte access mode.
	 * @throws IllegalArgumentException If the combination is invalid.
	 */
	public long getSigned(DataType type, DataOrder order, DataTransformation transformation) {
		long longValue = get(type, order, transformation);
		if (type != DataType.LONG) {
			int max = (int) (Math.pow(2, type.getBytes() * 8 - 1) - 1);
			if (longValue > max) {
				longValue -= (max + 1) * 2;
			}
		}
		return longValue;
	}

	/**
	 * Gets a signed data type from the buffer with the specified transformation.
	 *
	 * @param type The data type.
	 * @param transformation The data transformation.
	 * @return The value.
	 * @throws IllegalStateException If this reader is not in byte access mode.
	 * @throws IllegalArgumentException If the combination is invalid.
	 */
	public long getSigned(DataType type, DataTransformation transformation) {
		return getSigned(type, DataOrder.BIG, transformation);
	}

	/**
	 * Gets a signed smart from the buffer.
	 *
	 * @return The smart.
	 * @throws IllegalStateException If this reader is not in byte access mode.
	 */
	public int getSignedSmart() {
		checkByteAccess();
		int peek = buffer.getByte(buffer.readerIndex());
		if (peek < 128) {
			return buffer.readByte() - 64;
		}
		return buffer.readShort() - 49152;
	}

	/**
	 * Gets a string from the buffer.
	 *
	 * @return The string.
	 * @throws IllegalStateException If this reader is not in byte access mode.
	 */
	public String getString() {
		checkByteAccess();
		return BufferUtil.readString(buffer);
	}

	/**
	 * The C-String terminator value.
	 */
	public static final int TERMINATOR_VALUE = 10;

	/**
	 * Reads a series of byte data terminated by a null value, casted to a {@link String}.
	 */
	public String getCString() {
		byte temp;
		StringBuilder b = new StringBuilder();
		while(buffer.isReadable() && (temp = (byte) buffer.readUnsignedByte()) != TERMINATOR_VALUE) {
			b.append((char) temp);
		}
		return b.toString();
	}

	/**
	 * Gets an unsigned data type from the buffer.
	 *
	 * @param type The data type.
	 * @return The value.
	 * @throws IllegalStateException If this reader is not in byte access mode.
	 */
	public long getUnsigned(DataType type) {
		return getUnsigned(type, DataOrder.BIG, DataTransformation.NONE);
	}

	/**
	 * Gets an unsigned data type from the buffer with the specified order.
	 *
	 * @param type The data type.
	 * @param order The byte order.
	 * @return The value.
	 * @throws IllegalStateException If this reader is not in byte access mode.
	 * @throws IllegalArgumentException If the combination is invalid.
	 */
	public long getUnsigned(DataType type, DataOrder order) {
		return getUnsigned(type, order, DataTransformation.NONE);
	}

	/**
	 * Gets an unsigned data type from the buffer with the specified order and transformation.
	 *
	 * @param type The data type.
	 * @param order The byte order.
	 * @param transformation The data transformation.
	 * @return The value.
	 * @throws IllegalStateException If this reader is not in byte access mode.
	 * @throws IllegalArgumentException If the combination is invalid.
	 */
	public long getUnsigned(DataType type, DataOrder order, DataTransformation transformation) {
		long longValue = get(type, order, transformation);
		Preconditions.checkArgument(type != DataType.LONG, "Longs must be read as a signed type.");
		return longValue & 0xFFFFFFFFFFFFFFFFL;
	}

	/**
	 * Gets an unsigned data type from the buffer with the specified transformation.
	 *
	 * @param type The data type.
	 * @param transformation The data transformation.
	 * @return The value.
	 * @throws IllegalStateException If this reader is not in byte access mode.
	 * @throws IllegalArgumentException If the combination is invalid.
	 */
	public long getUnsigned(DataType type, DataTransformation transformation) {
		return getUnsigned(type, DataOrder.BIG, transformation);
	}

	/**
	 * Gets an unsigned smart from the buffer.
	 *
	 * @return The smart.
	 * @throws IllegalStateException If this reader is not in byte access mode.
	 */
	public int getUnsignedSmart() {
		checkByteAccess();
		int peek = buffer.getByte(buffer.readerIndex());
		if (peek < 128) {
			return buffer.readByte();
		}
		return buffer.readShort() - 32768;
	}

	/**
	 * Reads a value as a {@code byte}.
	 * @param signed if the byte is signed.
	 * @param type The byte transformation type
	 * @return The value of the byte.
	 */
	public int get(boolean signed, DataTransformation type) {
		int value = buffer.readByte();
		switch(type) {
			case ADD:
				value = value - 128;
				break;
			case NEGATE:
				value = -value;
				break;
			case SUBTRACT:
				value = 128 - value;
				break;
			case NONE:
				break;
		}
		return signed ? value : value & 0xff;
	}

	/**
	 * Reads a standard signed {@code byte}.
	 * @return The value of the byte.
	 */
	public int get() {
		return get(true, DataTransformation.NONE);
	}

	/**
	 * Reads a standard {@code byte}.
	 * @param signed If the byte is signed.
	 * @return The value of the byte.
	 */
	public int get(boolean signed) {
		return get(signed, DataTransformation.NONE);
	}

	/**
	 * Reads a signed {@code byte}.
	 * @param type The byte transformation type
	 * @return The value of the byte.
	 */
	public int get(DataTransformation type) {
		return get(true, type);
	}


	/**
	 * Reads a {@code short} value.
	 * @param signed If the short is signed.
	 * @param type The byte transformation type
	 * @param order The byte endianness type.
	 * @return The value of the short.
	 * @throws UnsupportedOperationException if middle or inverse-middle value types are selected.
	 */
	public int getShort(boolean signed, DataTransformation type, DataOrder order) {
		int value = 0;
		switch(order) {
			case BIG:
				value |= get(false) << 8;
				value |= get(false, type);
				break;
			case MIDDLE:
				throw new UnsupportedOperationException("Middle-endian short is impossible!");
			case INVERSED_MIDDLE:
				throw new UnsupportedOperationException("Inverse-middle-endian short is impossible!");
			case LITTLE:
				value |= get(false, type);
				value |= get(false) << 8;
				break;
		}
		return signed ? value : value & 0xffff;
	}

	/**
	 * Reads a standard {@code short}.
	 * @param signed If the short is signed.
	 * @param order The byte endianness type.
	 * @return The value of the short.
	 */
	public int getShort(boolean signed, DataOrder order) {
		if(signed) {
			return (int) getSigned(DataType.SHORT, order, DataTransformation.NONE);
		}
		return (int) getUnsigned(DataType.SHORT, order, DataTransformation.NONE);
	}

	/**
	 * Reads a standard signed big-endian {@code short}.
	 * @return The value of the short.
	 */
	public int getShort() {
		return getShort(true, DataTransformation.NONE, DataOrder.BIG);
	}

	/**
	 * Reads a standard big-endian {@code short}.
	 * @param signed If the short is signed.
	 * @return The value of the short.
	 */
	public int getShort(boolean signed) {
		return getShort(signed, DataTransformation.NONE, DataOrder.BIG);
	}

	/**
	 * Reads a signed big-endian {@code short}.
	 * @param type The byte transformation type
	 * @return The value of the short.
	 */
	public int getShort(DataTransformation type) {
		return getShort(true, type, DataOrder.BIG);
	}

	/**
	 * Reads a big-endian {@code short}.
	 * @param signed If the short is signed.
	 * @param type The byte transformation type
	 * @return The value of the short.
	 */
	public int getShort(boolean signed, DataTransformation type) {
		return getShort(signed, type, DataOrder.BIG);
	}

	/**
	 * Reads a signed standard {@code short}.
	 * @param order The byte endianness type.
	 * @return The value of the short.
	 */
	public int getShort(DataOrder order) {
		return getShort(true, DataTransformation.NONE, order);
	}

	/**
	 * Reads a signed {@code short}.
	 * @param type The byte transformation type
	 * @param order The byte endianness type.
	 * @return The value of the short.
	 */
	public int getShort(DataTransformation type, DataOrder order) {
		return getShort(true, type, order);
	}


	/**
	 * Reads an {@code int}.
	 * @param signed If the integer is signed.
	 * @param type The byte transformation type
	 * @param order The byte endianness type.
	 * @return The value of the integer.
	 */
	public int getInt(boolean signed, DataTransformation type, DataOrder order) {
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
			case INVERSED_MIDDLE:
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
	 * @return The value of the integer.
	 */
	public int getInt() {
		return getInt(true, DataTransformation.NONE, DataOrder.BIG);
	}

	/**
	 * Reads a standard big-endian {@code int}.
	 * @param signed If the integer is signed.
	 * @return The value of the integer.
	 */
	public int getInt(boolean signed) {
		return getInt(signed, DataTransformation.NONE, DataOrder.BIG);
	}

	/**
	 * Reads a signed big-endian {@code int}.
	 * @param type The byte transformation type
	 * @return The value of the integer.
	 */
	public int getInt(DataTransformation type) {
		return getInt(true, type, DataOrder.BIG);
	}

	/**
	 * Reads a big-endian {@code int}.
	 * @param signed If the integer is signed.
	 * @param type The byte transformation type
	 * @return The value of the integer.
	 */
	public int getInt(boolean signed, DataTransformation type) {
		return getInt(signed, type, DataOrder.BIG);
	}

	/**
	 * Reads a signed standard {@code int}.
	 * @param order The byte endianness type.
	 * @return The value of the integer.
	 */
	public int getInt(DataOrder order) {
		return getInt(true, DataTransformation.NONE, order);
	}

	/**
	 * Reads a standard {@code int}.
	 * @param signed If the integer is signed.
	 * @param order The byte endianness type.
	 * @return The value of the integer.
	 */
	public int getInt(boolean signed, DataOrder order) {
		return getInt(signed, DataTransformation.NONE, order);
	}

	/**
	 * Reads a signed {@code int}.
	 * @param type The byte transformation type
	 * @param order The byte endianness type.
	 * @return The value of the integer.
	 */
	public int getInt(DataTransformation type, DataOrder order) {
		return getInt(true, type, order);
	}

	/**
	 * Reads a signed {@code long} value.
	 * @param type The byte transformation type
	 * @param order The byte endianness type.
	 * @return The value of the long.
	 * @throws UnsupportedOperationException if middle or inverse-middle value types are selected.
	 */
	public long getLong(DataTransformation type, DataOrder order) {
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
			case INVERSED_MIDDLE:
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
	 * @return The value of the long.
	 */
	public long getLong() {
		return getLong(DataTransformation.NONE, DataOrder.BIG);
	}

	/**
	 * Reads a signed big-endian {@code long}.
	 * @param type The byte transformation type.
	 * @return The value of the long.
	 */
	public long getLong(DataTransformation type) {
		return getLong(type, DataOrder.BIG);
	}

	/**
	 * Reads a signed standard {@code long}.
	 * @param order The byte endianness type.
	 * @return The value of the long.
	 */
	public long getLong(DataOrder order) {
		return getLong(DataTransformation.NONE, order);
	}


	/**
	 * Switches this builder's mode to the bit access mode.
	 *
	 * @throws IllegalStateException If the builder is already in bit access mode.
	 */
	public void switchToBitAccess() {
		Preconditions.checkState(mode != AccessMode.BIT_ACCESS, "Already in bit access mode.");
		mode = AccessMode.BIT_ACCESS;
		bitIndex = buffer.readerIndex() * 8;
	}

	/**
	 * Switches this builder's mode to the byte access mode.
	 *
	 * @throws IllegalStateException If the builder is already in byte access mode.
	 */
	public void switchToByteAccess() {
		Preconditions.checkState(mode != AccessMode.BYTE_ACCESS, "Already in byte access mode.");
		mode = AccessMode.BYTE_ACCESS;
		buffer.readerIndex((bitIndex + 7) / 8);
	}

}