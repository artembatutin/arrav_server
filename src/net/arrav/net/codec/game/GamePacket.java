package net.arrav.net.codec.game;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.arrav.net.codec.ByteTransform;
import net.arrav.net.codec.crypto.IsaacRandom;
import net.arrav.net.packet.OutgoingPacket;

public class GamePacket {
	
	/**
	 * The default buffer size.
	 */
	public static final int DEFAULT_BUFFER_SIZE = 128;
	/**
	 * The C-String terminator value.
	 */
	public static final int TERMINATOR_VALUE = 10;
	/**
	 * An array of the bit masks used for writing bits.
	 */
	private static final int[] BIT_MASK = {0, 0x1, 0x3, 0x7, 0xf, 0x1f, 0x3f, 0x7f, 0xff, 0x1ff, 0x3ff, 0x7ff, 0xfff, 0x1fff, 0x3fff, 0x7fff, 0xffff, 0x1ffff, 0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff, -1};
	
	/**
	 * The opcode, {@code -1} if this message does not have an opcode.
	 */
	private int opcode;
	
	/**
	 * The current bit position when writing bits.
	 */
	private int bitIndex = -1;
	
	/**
	 * The {@link GamePacketType} of the message currently being built.
	 */
	private GamePacketType type;
	
	/**
	 * The {@link ByteBuf} buffer of this packet.
	 */
	private ByteBuf payload;
	
	/**
	 * Creates a new outgoing {@link GamePacket} based on recommended {@link OutgoingPacket} size.
	 */
	public GamePacket(OutgoingPacket out, ByteBuf buf) {
		this.payload = buf;
	}
	
	/**
	 * Creates a new incoming {@link GamePacket}
	 */
	public GamePacket(int opcode, ByteBuf payload, GamePacketType type) {
		this.payload = payload;
		this.opcode = opcode;
		this.type = type;
	}
	
	/**
	 * Constructs a new fixed sized message.
	 * @param id The id of the message.
	 */
	public void message(int id) {
		message(id, GamePacketType.FIXED);
	}
	
	/**
	 * Constructs a new message.
	 * @param id The id of the message.
	 * @param type The type of message to build.
	 */
	public void message(int id, GamePacketType type) {
		if(type == GamePacketType.RAW) {
			throw new IllegalArgumentException();
		}
		this.opcode = id;
		this.type = type;
		//this.sizeIndex = buf.writerIndex();
	}
	
	public ByteBuf writePacket(ByteBuf out, IsaacRandom encryptor) {
		out.writeByte(opcode + encryptor.nextInt());
		int size = payload.readableBytes();
		//System.out.println("Encoding packet: " + opcode + " with size " + size);
		if(type == GamePacketType.VARIABLE_BYTE) {
			out.writeByte(size);
		} else if(type == GamePacketType.VARIABLE_SHORT) {
			out.writeShort(size);
		}
		if(size > 0) {
			out.writeBytes(payload);
		}
		return out;
	}
	
	/**
	 * Ends the building of a message where the size may vary.
	 */
	public void endVarSize() {
		//int recordedSize = buf.writerIndex() - sizeIndex;
		//if(type == GamePacketType.VARIABLE_BYTE) {
		//	buf.setByte(sizeIndex, recordedSize - 1);
		//} else if(type == GamePacketType.VARIABLE_SHORT) {
		//	setShort(sizeIndex, recordedSize - 2);
		//}
	}
	
	/**
	 * Prepares the buffer for writing bits.
	 */
	public void startBitAccess() {
		bitIndex = payload.writerIndex() << 3;
	}
	
	/**
	 * Prepares the buffer for writing bytes.
	 */
	public void endBitAccess() {
		payload.writerIndex((bitIndex + 7) >> 3);
		bitIndex = -1;
	}
	
	/**
	 * Writes the bytes from the argued buffer into this buffer.
	 * @param from the argued buffer that bytes will be written from.
	 * @return an instance of this message builder.
	 */
	public void putBytes(byte[] from, int size) {
		payload.writeBytes(from, 0, size);
	}
	
	/**
	 * Puts the bytes from another {@link GamePacket}
	 * @param packet packet.
	 */
	public void putBytes(GamePacket packet) {
		payload.writeBytes(packet.getPayload());
	}
	
	/**
	 * Puts the bytes from the specified buffer into this packet's buffer.
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
	 * Writes the bytes from the argued buffer into this buffer.
	 * @param from The argued buffer that bytes will be written from.
	 * @return An instance of this byte message.
	 */
	public void putBytes(byte[] from) {
		payload.writeBytes(from, 0, from.length);
	}
	
	/**
	 * Writes the bytes from the argued byte array into this buffer, in reverse.
	 * @param data The data to write to this buffer.
	 */
	public void putBytesReverse(byte[] data) {
		for(int i = data.length - 1; i >= 0; i--) {
			put(data[i]);
		}
	}
	
	/**
	 * Puts {@code numBits} into the buffer with the value {@code value}.
	 * @param numBits The number of bits to put into the buffer.
	 * @param value The value.
	 * @throws IllegalArgumentException If the number of bits is not between 1 and 31 inclusive.
	 */
	public void putBits(int numBits, int value) {
		int bytes = (int) Math.ceil((double) numBits / 8D) + 1;
		payload.ensureWritable((bitIndex + 7) / 8 + bytes);
		
		int bytePos = bitIndex >> 3;
		int bitOffset = 8 - (bitIndex & 7);
		bitIndex += numBits;
		
		while(numBits > bitOffset) {
			int temp = payload.getByte(bytePos);
			temp &= ~BIT_MASK[bitOffset];
			temp |= (value >> (numBits - bitOffset)) & BIT_MASK[bitOffset];
			payload.setByte(bytePos++, temp);
			numBits -= bitOffset;
			bitOffset = 8;
		}
		int temp = payload.getByte(bytePos);
		if(numBits == bitOffset) {
			temp &= ~BIT_MASK[bitOffset];
			temp |= value & BIT_MASK[bitOffset];
			payload.setByte(bytePos, temp);
		} else {
			temp &= ~(BIT_MASK[numBits] << (bitOffset - numBits));
			temp |= (value & BIT_MASK[numBits]) << (bitOffset - numBits);
			payload.setByte(bytePos, temp);
		}
	}
	
	/**
	 * Writes a boolean bit flag.
	 * @param flag The flag to write.
	 * @return An instance of this byte message.
	 */
	public void putBit(boolean flag) {
		putBits(1, flag ? 1 : 0);
	}
	
	/**
	 * Writes a value as a {@code byte}.
	 * @param value The value to write.
	 * @param type The byte transformation type
	 * @return An instance of this byte message.
	 */
	public void put(int value, ByteTransform type) {
		switch(type) {
			case A:
				value += 128;
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
		payload.writeByte((byte) value);
	}
	
	/**
	 * Writes a value as a normal {@code byte}.
	 * @param value The value to write.
	 * @return An instance of this byte message.
	 */
	public void put(int value) {
		put(value, ByteTransform.NORMAL);
	}
	
	/**
	 * Writes a value as a {@code short}.
	 * @param value The value to write.
	 * @param type The byte transformation type
	 * @param order The byte endianness type.
	 * @return An instance of this byte message.
	 * @throws UnsupportedOperationException If middle or inverse-middle value types are selected.
	 */
	public void putShort(int value, ByteTransform type, net.arrav.net.codec.ByteOrder order) {
		switch(order) {
			case BIG:
				put(value >> 8);
				put(value, type);
				break;
			case MIDDLE:
				throw new UnsupportedOperationException("Middle-endian short is impossible.");
			case INVERSE_MIDDLE:
				throw new UnsupportedOperationException("Inversed-middle-endian short is impossible.");
			case LITTLE:
				put(value, type);
				put(value >> 8);
				break;
		}
	}
	
	/**
	 * Writes a value as a normal big-endian {@code short}.
	 * @param value The value to write.
	 * @return An instance of this byte message.
	 */
	public void putShort(int value) {
		putShort(value, ByteTransform.NORMAL, net.arrav.net.codec.ByteOrder.BIG);
	}
	
	/**
	 * Writes a value as a big-endian {@code short}.
	 * @param value The value to write.
	 * @param type The byte transformation type
	 * @return An instance of this byte message.
	 */
	public void putShort(int value, ByteTransform type) {
		putShort(value, type, net.arrav.net.codec.ByteOrder.BIG);
	}
	
	/**
	 * Writes a value as a standard {@code short}.
	 * @param value The value to write.
	 * @param order The byte endianness type.
	 * @return An instance of this byte message.
	 */
	public void putShort(int value, net.arrav.net.codec.ByteOrder order) {
		putShort(value, ByteTransform.NORMAL, order);
	}
	
	/**
	 * Writes a value as an {@code int}.
	 * @param value The value to write.
	 * @param type The byte transformation type
	 * @param order The byte endianness type.
	 * @return An instance of this byte message.
	 */
	public void putInt(int value, ByteTransform type, net.arrav.net.codec.ByteOrder order) {
		switch(order) {
			case BIG:
				put(value >> 24);
				put(value >> 16);
				put(value >> 8);
				put(value, type);
				break;
			case MIDDLE:
				put(value >> 8);
				put(value, type);
				put(value >> 24);
				put(value >> 16);
				break;
			case INVERSE_MIDDLE:
				put(value >> 16);
				put(value >> 24);
				put(value, type);
				put(value >> 8);
				break;
			case LITTLE:
				put(value, type);
				put(value >> 8);
				put(value >> 16);
				put(value >> 24);
				break;
		}
	}
	
	/**
	 * Writes a value as a standard big-endian {@code int}.
	 * @param value The value to write.
	 * @return An instance of this byte message.
	 */
	public void putInt(int value) {
		putInt(value, ByteTransform.NORMAL, net.arrav.net.codec.ByteOrder.BIG);
	}
	
	/**
	 * Writes a value as a big-endian {@code int}.
	 * @param value The value to write.
	 * @param type The byte transformation type
	 * @return An instance of this byte message.
	 */
	public void putInt(int value, ByteTransform type) {
		putInt(value, type, net.arrav.net.codec.ByteOrder.BIG);
	}
	
	/**
	 * Writes a value as a standard {@code int}.
	 * @param value The value to write.
	 * @param order The byte endianness type.
	 * @return An instance of this byte message.
	 */
	public void putInt(int value, net.arrav.net.codec.ByteOrder order) {
		putInt(value, ByteTransform.NORMAL, order);
	}
	
	/**
	 * Writes a value as a {@code long}.
	 * @param value The value to write.
	 * @param type The byte transformation type
	 * @param order The byte endianness type.
	 * @return An instance of this byte message.
	 * @throws UnsupportedOperationException If middle or inverse-middle value types are selected.
	 */
	public void putLong(long value, ByteTransform type, net.arrav.net.codec.ByteOrder order) {
		switch(order) {
			case BIG:
				put((int) (value >> 56));
				put((int) (value >> 48));
				put((int) (value >> 40));
				put((int) (value >> 32));
				put((int) (value >> 24));
				put((int) (value >> 16));
				put((int) (value >> 8));
				put((int) value, type);
				break;
			case MIDDLE:
				throw new UnsupportedOperationException("Middle-endian long is not implemented!");
			case INVERSE_MIDDLE:
				throw new UnsupportedOperationException("Inverse-middle-endian long is not implemented!");
			case LITTLE:
				put((int) value, type);
				put((int) (value >> 8));
				put((int) (value >> 16));
				put((int) (value >> 24));
				put((int) (value >> 32));
				put((int) (value >> 40));
				put((int) (value >> 48));
				put((int) (value >> 56));
				break;
		}
	}
	
	/**
	 * Writes a value as a standard big-endian {@code long}.
	 * @param value The value to write.
	 * @return An instance of this byte message.
	 */
	public void putLong(long value) {
		putLong(value, ByteTransform.NORMAL, net.arrav.net.codec.ByteOrder.BIG);
	}
	
	/**
	 * Writes a value as a big-endian {@code long}.
	 * @param value The value to write.
	 * @param type The byte transformation type
	 * @return An instance of this byte message.
	 */
	public void putLong(long value, ByteTransform type) {
		putLong(value, type, net.arrav.net.codec.ByteOrder.BIG);
	}
	
	/**
	 * Writes a value as a standard {@code long}.
	 * @param value The value to write.
	 * @param order The byte endianness type. to write.
	 * @return An instance of this byte message.
	 */
	public void putLong(long value, net.arrav.net.codec.ByteOrder order) {
		putLong(value, ByteTransform.NORMAL, order);
	}
	
	/**
	 * Writes a RuneScape {@code String} value.
	 * @param string The string to write.
	 */
	public void putCString(String string) {
		for(byte value : string.getBytes()) {
			put(value);
		}
		put(TERMINATOR_VALUE);
	}
	
	/**
	 * Reads a series of byte data terminated by a null value, casted to a {@link String}.
	 */
	public String getCString() {
		byte temp;
		StringBuilder b = new StringBuilder();
		while(payload.isReadable() && (temp = (byte) payload.readUnsignedByte()) != TERMINATOR_VALUE) {
			b.append((char) temp);
		}
		return b.toString();
	}
	
	/**
	 * Reads a value as a {@code byte}.
	 * @param signed if the byte is signed.
	 * @param type The byte transformation type
	 * @return The value of the byte.
	 */
	public int get(boolean signed, ByteTransform type) {
		int value = payload.readByte();
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
	 * @return The value of the byte.
	 */
	public int get() {
		return get(true, ByteTransform.NORMAL);
	}
	
	/**
	 * Reads a standard {@code byte}.
	 * @param signed If the byte is signed.
	 * @return The value of the byte.
	 */
	public int get(boolean signed) {
		return get(signed, ByteTransform.NORMAL);
	}
	
	/**
	 * Reads a signed {@code byte}.
	 * @param type The byte transformation type
	 * @return The value of the byte.
	 */
	public int get(ByteTransform type) {
		return get(true, type);
	}
	
	/**
	 * Reads a {@code medium} / {@code tri-byte} value.
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
	 * @param signed If the short is signed.
	 * @param type The byte transformation type
	 * @param order The byte endianness type.
	 * @return The value of the short.
	 * @throws UnsupportedOperationException if middle or inverse-middle value types are selected.
	 */
	public int getShort(boolean signed, ByteTransform type, net.arrav.net.codec.ByteOrder order) {
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
	 * @return The value of the short.
	 */
	public int getShort() {
		return getShort(true, ByteTransform.NORMAL, net.arrav.net.codec.ByteOrder.BIG);
	}
	
	/**
	 * Reads a standard big-endian {@code short}.
	 * @param signed If the short is signed.
	 * @return The value of the short.
	 */
	public int getShort(boolean signed) {
		return getShort(signed, ByteTransform.NORMAL, net.arrav.net.codec.ByteOrder.BIG);
	}
	
	/**
	 * Reads a signed big-endian {@code short}.
	 * @param type The byte transformation type
	 * @return The value of the short.
	 */
	public int getShort(ByteTransform type) {
		return getShort(true, type, net.arrav.net.codec.ByteOrder.BIG);
	}
	
	/**
	 * Reads a big-endian {@code short}.
	 * @param signed If the short is signed.
	 * @param type The byte transformation type
	 * @return The value of the short.
	 */
	public int getShort(boolean signed, ByteTransform type) {
		return getShort(signed, type, net.arrav.net.codec.ByteOrder.BIG);
	}
	
	/**
	 * Reads a signed standard {@code short}.
	 * @param order The byte endianness type.
	 * @return The value of the short.
	 */
	public int getShort(net.arrav.net.codec.ByteOrder order) {
		return getShort(true, ByteTransform.NORMAL, order);
	}
	
	/**
	 * Reads a standard {@code short}.
	 * @param signed If the short is signed.
	 * @param order The byte endianness type.
	 * @return The value of the short.
	 */
	public int getShort(boolean signed, net.arrav.net.codec.ByteOrder order) {
		return getShort(signed, ByteTransform.NORMAL, order);
	}
	
	/**
	 * Reads a signed {@code short}.
	 * @param type The byte transformation type
	 * @param order The byte endianness type.
	 * @return The value of the short.
	 */
	public int getShort(ByteTransform type, net.arrav.net.codec.ByteOrder order) {
		return getShort(true, type, order);
	}
	
	/**
	 * Reads an {@code int}.
	 * @param signed If the integer is signed.
	 * @param type The byte transformation type
	 * @param order The byte endianness type.
	 * @return The value of the integer.
	 */
	public int getInt(boolean signed, ByteTransform type, net.arrav.net.codec.ByteOrder order) {
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
	 * @return The value of the integer.
	 */
	public int getInt() {
		return getInt(true, ByteTransform.NORMAL, net.arrav.net.codec.ByteOrder.BIG);
	}
	
	/**
	 * Reads a standard big-endian {@code int}.
	 * @param signed If the integer is signed.
	 * @return The value of the integer.
	 */
	public int getInt(boolean signed) {
		return getInt(signed, ByteTransform.NORMAL, net.arrav.net.codec.ByteOrder.BIG);
	}
	
	/**
	 * Reads a signed big-endian {@code int}.
	 * @param type The byte transformation type
	 * @return The value of the integer.
	 */
	public int getInt(ByteTransform type) {
		return getInt(true, type, net.arrav.net.codec.ByteOrder.BIG);
	}
	
	/**
	 * Reads a big-endian {@code int}.
	 * @param signed If the integer is signed.
	 * @param type The byte transformation type
	 * @return The value of the integer.
	 */
	public int getInt(boolean signed, ByteTransform type) {
		return getInt(signed, type, net.arrav.net.codec.ByteOrder.BIG);
	}
	
	/**
	 * Reads a signed standard {@code int}.
	 * @param order The byte endianness type.
	 * @return The value of the integer.
	 */
	public int getInt(net.arrav.net.codec.ByteOrder order) {
		return getInt(true, ByteTransform.NORMAL, order);
	}
	
	/**
	 * Reads a standard {@code int}.
	 * @param signed If the integer is signed.
	 * @param order The byte endianness type.
	 * @return The value of the integer.
	 */
	public int getInt(boolean signed, net.arrav.net.codec.ByteOrder order) {
		return getInt(signed, ByteTransform.NORMAL, order);
	}
	
	/**
	 * Reads a signed {@code int}.
	 * @param type The byte transformation type
	 * @param order The byte endianness type.
	 * @return The value of the integer.
	 */
	public int getInt(ByteTransform type, net.arrav.net.codec.ByteOrder order) {
		return getInt(true, type, order);
	}
	
	/**
	 * Reads a signed {@code long} value.
	 * @param type The byte transformation type
	 * @param order The byte endianness type.
	 * @return The value of the long.
	 * @throws UnsupportedOperationException if middle or inverse-middle value types are selected.
	 */
	public long getLong(ByteTransform type, net.arrav.net.codec.ByteOrder order) {
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
	 * @return The value of the long.
	 */
	public long getLong() {
		return getLong(ByteTransform.NORMAL, net.arrav.net.codec.ByteOrder.BIG);
	}
	
	/**
	 * Reads a signed big-endian {@code long}.
	 * @param type The byte transformation type.
	 * @return The value of the long.
	 */
	public long getLong(ByteTransform type) {
		return getLong(type, net.arrav.net.codec.ByteOrder.BIG);
	}
	
	/**
	 * Reads a signed standard {@code long}.
	 * @param order The byte endianness type.
	 * @return The value of the long.
	 */
	public long getLong(net.arrav.net.codec.ByteOrder order) {
		return getLong(ByteTransform.NORMAL, order);
	}
	
	/**
	 * Reads the amount of bytes into the array, starting at the current position.
	 * @param amount The amount to read.
	 * @return A buffer filled with the data.
	 */
	public byte[] getBytes(int amount) {
		return getBytes(amount, ByteTransform.NORMAL);
	}
	
	/**
	 * Reads the amount of bytes into a byte array, starting at the current position.
	 * @param amount The amount of bytes.
	 * @param type The byte transformation type of each byte.
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
	 * @param amount The amount of bytes to read.
	 * @param type The byte transformation type of each byte.
	 * @return A buffer filled with the data.
	 */
	public byte[] getBytesReverse(int amount, ByteTransform type) {
		byte[] data = new byte[amount];
		int dataPosition = 0;
		for(int i = payload.readerIndex() + amount - 1; i >= payload.readerIndex(); i--) {
			int value = payload.getByte(i);
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
	
	public int getOpcode() {
		return opcode;
	}
	
	public ByteBuf getPayload() {
		return payload;
	}
	
	static {
		// Initialize bit masks.
		for(int i = 0; i < BIT_MASK.length; i++) {
			BIT_MASK[i] = (1 << i) - 1;
		}
	}
	
}
