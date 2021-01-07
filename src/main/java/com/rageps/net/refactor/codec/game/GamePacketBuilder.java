package com.rageps.net.refactor.codec.game;

import com.google.common.base.Preconditions;
import com.rageps.net.refactor.meta.PacketType;
import com.rageps.util.BufferUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * A class which assists in creating a {@link GamePacket}.
 *
 * @author Graham
 */
public final class GamePacketBuilder {

	/**
	 * The current bit index.
	 */
	private int bitIndex;

	/**
	 * The buffer.
	 */
	private final ByteBuf buffer = Unpooled.buffer();

	/**
	 * The current mode.
	 */
	private AccessMode mode = AccessMode.BYTE_ACCESS;

	/**
	 * The opcode.
	 */
	private final int opcode;

	/**
	 * The {@link PacketType}.
	 */
	private final PacketType type;

	/**
	 * Creates a raw {@link GamePacketBuilder}.
	 */
	public GamePacketBuilder() {
		opcode = -1;
		type = PacketType.RAW;
	}

	/**
	 * Creates the {@link GamePacketBuilder} for a {@link PacketType#FIXED} packet with the specified opcode.
	 *
	 * @param opcode The opcode.
	 */
	public GamePacketBuilder(int opcode) {
		this(opcode, PacketType.FIXED);
	}

	/**
	 * Creates the {@link GamePacketBuilder} for the specified packet type and opcode.
	 *
	 * @param opcode The opcode.
	 * @param type The packet type.
	 */
	public GamePacketBuilder(int opcode, PacketType type) {
		this.opcode = opcode;
		this.type = type;
	}

	/**
	 * Checks that this builder is in the bit access mode.
	 *
	 * @throws IllegalStateException If the builder is not in bit access mode.
	 */
	private void checkBitAccess() {
		Preconditions.checkState(mode == AccessMode.BIT_ACCESS, "For bit-based calls to work, the mode must be bit access.");
	}

	/**
	 * Checks that this builder is in the byte access mode.
	 *
	 * @throws IllegalStateException If the builder is not in byte access mode.
	 */
	private void checkByteAccess() {
		Preconditions.checkState(mode == AccessMode.BYTE_ACCESS, "For byte-based calls to work, the mode must be byte access.");
	}

	/**
	 * Gets the current length of the builder's buffer.
	 *
	 * @return The length of the buffer.
	 */
	public int getLength() {
		checkByteAccess();
		return buffer.writerIndex();
	}

	/**
	 * Puts a standard data type with the specified value, byte order and transformation.
	 *
	 * @param type The data type.
	 * @param order The byte order.
	 * @param transformation The transformation.
	 * @param value The value.
	 * @throws IllegalArgumentException If the type, order, or transformation is unknown.
	 */
	public void put(DataType type, DataOrder order, DataTransformation transformation, Number value) {
		checkByteAccess();
		long longValue = value.longValue();
		int length = type.getBytes();
		if (order == DataOrder.BIG) {
			for (int i = length - 1; i >= 0; i--) {
				if (i == 0 && transformation != DataTransformation.NONE) {
					if (transformation == DataTransformation.ADD) {
						buffer.writeByte((byte) (longValue + 128));
					} else if (transformation == DataTransformation.NEGATE) {
						buffer.writeByte((byte) -longValue);
					} else if (transformation == DataTransformation.SUBTRACT) {
						buffer.writeByte((byte) (128 - longValue));
					} else {
						throw new IllegalArgumentException("Unknown transformation.");
					}
				} else {
					buffer.writeByte((byte) (longValue >> i * 8));
				}
			}
		} else if (order == DataOrder.LITTLE) {
			for (int i = 0; i < length; i++) {
				if (i == 0 && transformation != DataTransformation.NONE) {
					if (transformation == DataTransformation.ADD) {
						buffer.writeByte((byte) (longValue + 128));
					} else if (transformation == DataTransformation.NEGATE) {
						buffer.writeByte((byte) -longValue);
					} else if (transformation == DataTransformation.SUBTRACT) {
						buffer.writeByte((byte) (128 - longValue));
					} else {
						throw new IllegalArgumentException("Unknown transformation.");
					}
				} else {
					buffer.writeByte((byte) (longValue >> i * 8));
				}
			}
		} else if (order == DataOrder.MIDDLE) {
			Preconditions.checkArgument(transformation == DataTransformation.NONE, "Middle endian cannot be transformed.");

			Preconditions.checkArgument(type == DataType.INT, "Middle endian can only be used with an integer.");

			buffer.writeByte((byte) (longValue >> 8));
			buffer.writeByte((byte) longValue);
			buffer.writeByte((byte) (longValue >> 24));
			buffer.writeByte((byte) (longValue >> 16));
		} else if (order == DataOrder.INVERSED_MIDDLE) {
			Preconditions.checkArgument(transformation == DataTransformation.NONE, "Inversed middle endian cannot be transformed.");

			Preconditions.checkArgument(type == DataType.INT, "Inversed middle endian can only be used with an integer.");

			buffer.writeByte((byte) (longValue >> 16));
			buffer.writeByte((byte) (longValue >> 24));
			buffer.writeByte((byte) longValue);
			buffer.writeByte((byte) (longValue >> 8));
		} else {
			throw new IllegalArgumentException("Unknown order.");
		}
	}

	/**
	 * Writes a value as a {@code short}.
	 * @param value The value to write.
	 * @param type The byte transformation type
	 * @param order The byte endianness type.
	 * @return An instance of this byte message.
	 * @throws UnsupportedOperationException If middle or inverse-middle value types are selected.
	 */
	public void putShort(int value, DataTransformation type, DataOrder order) {
		switch(order) {
			case BIG:
				put(value >> 8);
				put(value, type);
				break;
			case MIDDLE:
				throw new UnsupportedOperationException("Middle-endian short is impossible.");
			case INVERSED_MIDDLE:
				throw new UnsupportedOperationException("Inversed-middle-endian short is impossible.");
			case LITTLE:
				put(value, type);
				put(value >> 8);
				break;
		}
	}

	/**
	 * Puts a standard data type with the specified value and byte order.
	 *
	 * @param type The data type.
	 * @param order The byte order.
	 * @param value The value.
	 */
	public void put(DataType type, DataOrder order, Number value) {
		put(type, order, DataTransformation.NONE, value);
	}

	/**
	 * Puts a standard data type with the specified value and transformation.
	 *
	 * @param type The type.
	 * @param transformation The transformation.
	 * @param value The value.
	 */
	public void put(DataType type, DataTransformation transformation, Number value) {
		put(type, DataOrder.BIG, transformation, value);
	}

	/**
	 * Puts a standard data type with the specified value.
	 *
	 * @param type The data type.
	 * @param value The value.
	 */
	public void put(DataType type, Number value) {
		put(type, DataOrder.BIG, DataTransformation.NONE, value);
	}

	/**
	 * Puts a single bit into the buffer. If {@code flag} is {@code true}, the value of the bit is {@code 1}. If
	 * {@code flag} is {@code false}, the value of the bit is {@code 0}.
	 *
	 * @param flag The flag.
	 */
	public void putBit(boolean flag) {
		putBit(flag ? 1 : 0);
	}

	/**
	 * Puts a single bit into the buffer with the value {@code value}.
	 *
	 * @param value The value.
	 */
	public void putBit(int value) {
		putBits(1, value);
	}

	/**
	 * Puts {@code numBits} into the buffer with the value {@code value}.
	 *
	 * @param numBits The number of bits to put into the buffer.
	 * @param value The value.
	 * @throws IllegalArgumentException If the number of bits is not between 1 and 31 inclusive.
	 */
	public void putBits(int numBits, int value) {
		Preconditions.checkArgument(numBits >= 1 && numBits <= 32, "Number of bits must be between 1 and 32 inclusive.");

		checkBitAccess();

		int bytePos = bitIndex >> 3;
		int bitOffset = 8 - (bitIndex & 7);
		bitIndex += numBits;

		int requiredSpace = bytePos - buffer.writerIndex() + 1;
		requiredSpace += (numBits + 7) / 8;
		buffer.ensureWritable(requiredSpace);

		for (; numBits > bitOffset; bitOffset = 8) {
			int tmp = buffer.getByte(bytePos);
			tmp &= ~DataConstants.BIT_MASK[bitOffset];
			tmp |= value >> numBits - bitOffset & DataConstants.BIT_MASK[bitOffset];
			buffer.setByte(bytePos++, tmp);
			numBits -= bitOffset;
		}
		int tmp = buffer.getByte(bytePos);
		if (numBits == bitOffset) {
			tmp &= ~DataConstants.BIT_MASK[bitOffset];
			tmp |= value & DataConstants.BIT_MASK[bitOffset];
			buffer.setByte(bytePos, tmp);
		} else {
			tmp &= ~(DataConstants.BIT_MASK[numBits] << bitOffset - numBits);
			tmp |= (value & DataConstants.BIT_MASK[numBits]) << bitOffset - numBits;
			buffer.setByte(bytePos, tmp);
		}
	}

	/**
	 * Puts the specified byte array into the buffer.
	 *
	 * @param bytes The byte array.
	 */
	public void putBytes(byte[] bytes) {
		buffer.writeBytes(bytes);
	}

	/**
	 * Puts the bytes from the specified buffer into this packet's buffer.
	 *
	 * @param buffer The source {@link ByteBuf}.
	 */
	public void putBytes(ByteBuf buffer) {
		byte[] bytes = new byte[buffer.readableBytes()];
		buffer.markReaderIndex();
		try {
			buffer.readBytes(bytes);
		} finally {
			buffer.resetReaderIndex();
		}
		putBytes(bytes);
	}

	/**
	 * Puts the bytes into the buffer with the specified transformation.
	 *
	 * @param transformation The transformation.
	 * @param bytes The byte array.
	 */
	public void putBytes(DataTransformation transformation, byte[] bytes) {
		if (transformation == DataTransformation.NONE) {
			putBytes(bytes);
		} else {
			for (byte b : bytes) {
				put(DataType.BYTE, transformation, b);
			}
		}
	}

	/**
	 * Writes the bytes from the argued buffer into this buffer.
	 * @param from the argued buffer that bytes will be written from.
	 * @return an instance of this message builder.
	 */
	public void putBytes(byte[] from, int size) {
		buffer.writeBytes(from, 0, size);
	}

	/**
	 * Puts the specified byte array into the buffer in reverse.
	 *
	 * @param bytes The byte array.
	 */
	public void putBytesReverse(byte[] bytes) {
		checkByteAccess();
		for (int i = bytes.length - 1; i >= 0; i--) {
			buffer.writeByte(bytes[i]);
		}
	}

	/**
	 * Puts the bytes from the specified buffer into this packet's buffer, in reverse.
	 *
	 * @param buffer The source {@link ByteBuf}.
	 */
	public void putBytesReverse(ByteBuf buffer) {
		byte[] bytes = new byte[buffer.readableBytes()];
		buffer.markReaderIndex();
		try {
			buffer.readBytes(bytes);
		} finally {
			buffer.resetReaderIndex();
		}
		putBytesReverse(bytes);
	}

	/**
	 * Puts the specified byte array into the buffer in reverse with the specified transformation.
	 *
	 * @param transformation The transformation.
	 * @param bytes The byte array.
	 */
	public void putBytesReverse(DataTransformation transformation, byte[] bytes) {
		if (transformation == DataTransformation.NONE) {
			putBytesReverse(bytes);
		} else {
			for (int i = bytes.length - 1; i >= 0; i--) {
				put(DataType.BYTE, transformation, bytes[i]);
			}
		}
	}

	/**
	 * Puts a raw builder. Both builders (this and parameter) must be in byte access mode.
	 *
	 * @param builder The builder.
	 * @throws IllegalArgumentException If the builder is not raw.
	 */
	public void putRawBuilder(GamePacketBuilder builder) {
		checkByteAccess();
		if (builder.type != PacketType.RAW) {
			throw new IllegalArgumentException("Builder must be raw.");
		}
		builder.checkByteAccess();
		putBytes(builder.buffer);
	}

	/**
	 * Puts a raw builder in reverse. Both builders (this and parameter) must be in byte access mode.
	 *
	 * @param builder The builder.
	 * @throws IllegalArgumentException If the builder is not raw.
	 */
	public void putRawBuilderReverse(GamePacketBuilder builder) {
		checkByteAccess();

		Preconditions.checkArgument(builder.type == PacketType.RAW, "Builder must be raw.");

		builder.checkByteAccess();
		putBytesReverse(builder.buffer);
	}

	/**
	 * Puts a smart into the buffer.
	 *
	 * @param value The value.
	 */
	public void putSmart(int value) {
		checkByteAccess();
		if (value < 128) {
			buffer.writeByte(value);
		} else {
			buffer.writeShort(value);
		}
	}

	/**
	 * Puts a string into the buffer.
	 *
	 * @param str The string.
	 */
	public void putString(String str) {
		checkByteAccess();
		char[] chars = str.toCharArray();
		for (char c : chars) {
			buffer.writeByte((byte) c);
		}
		buffer.writeByte(BufferUtil.STRING_TERMINATOR);
	}

	/**
	 * Writes a value as a {@code long}.
	 * @param value The value to write.
	 */
	public void putLong(long value) {
		put(DataType.LONG, value);
	}

	/**
	 * Writes a value as a normal big-endian {@code short}.
	 * @param value The value to write.
	 * @return An instance of this byte message.
	 */
	public void putShort(int value) {
		put(DataType.SHORT, value);
	}

	/**
	 * Writes a value as a normal {@code byte}.
	 * @param value The value to write.
	 * @return An instance of this byte message.
	 */
	public void put(int value) {
		put(DataType.BYTE, value);
	}

	/**
	 * Writes a value as a standard {@code short}.
	 * @param value The value to write.
	 * @param order The byte endianness type.
	 * @return An instance of this byte message.
	 */
	public void putShort(int value, DataOrder order) {
		put(DataType.SHORT, order, value);
	}

	/**
	 * Writes a value as a big-endian {@code short}.
	 * @param value The value to write.
	 * @param type The byte transformation type
	 * @return An instance of this byte message.
	 */
	public void putShort(int value, DataTransformation type) {
		put(DataType.SHORT, type, value);
	}

	/**
	 * Writes a value as a standard {@code int}.
	 * @param value The value to write.
	 * @param order The byte endianness type.
	 * @return An instance of this byte message.
	 */
	public void putInt(int value, DataOrder order) {
		put(DataType.INT, order, value);
	}

	/**
	 * Writes a value as a standard {@code int}.
	 * @param value The value to write.
	 * @return An instance of this byte message.
	 */
	public void putInt(int value) {
		put(DataType.INT, value);
	}

	/**
	 * Writes a value as a {@code byte}.
	 * @param value The value to write.
	 * @param type The byte transformation type
	 * @return An instance of this byte message.
	 * todo - test this is working correctly
	 */
	public void put(int value, DataTransformation type) {
		switch(type) {
			case ADD:
				value += 128;
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
		put(DataType.BYTE, DataTransformation.NONE, value);
	}

	/**
	 * Switches this builder's mode to the bit access mode.
	 *
	 * @throws IllegalStateException If the builder is already in bit access mode.
	 */
	public void switchToBitAccess() {
		Preconditions.checkState(mode != AccessMode.BIT_ACCESS, "Already in bit access mode.");

		mode = AccessMode.BIT_ACCESS;
		bitIndex = buffer.writerIndex() * 8;
	}

	/**
	 * Switches this builder's mode to the byte access mode.
	 *
	 * @throws IllegalStateException If the builder is already in byte access mode.
	 */
	public void switchToByteAccess() {
		Preconditions.checkState(mode != AccessMode.BYTE_ACCESS, "Already in bit access mode.");

		mode = AccessMode.BYTE_ACCESS;
		buffer.writerIndex((bitIndex + 7) / 8);
	}

	/**
	 * Creates a {@link GamePacket} based on the current contents of this builder.
	 *
	 * @return The {@link GamePacket}.
	 * @throws IllegalStateException If the builder is not in byte access mode, or if the packet is raw.
	 */
	public GamePacket toGamePacket() {
		Preconditions.checkState(type != PacketType.RAW, "Raw packets cannot be converted to a game packet.");

		Preconditions.checkState(mode == AccessMode.BYTE_ACCESS, "Must be in byte access mode to convert to a packet.");

		return new GamePacket(opcode, type, buffer);
	}

}