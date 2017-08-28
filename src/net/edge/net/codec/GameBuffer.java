package net.edge.net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.edge.net.codec.crypto.IsaacRandom;
import net.edge.net.packet.PacketUtils;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 5-7-2017.
 */
public final class GameBuffer {
	
	/**
	 * An array of the bit masks used for writing bits.
	 */
	private static final int[] BIT_MASK = {0, 0x1, 0x3, 0x7, 0xf, 0x1f, 0x3f, 0x7f, 0xff, 0x1ff, 0x3ff, 0x7ff, 0xfff, 0x1fff, 0x3fff, 0x7fff, 0xffff, 0x1ffff, 0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff, -1};
	
	/**
	 * The backing byte buffer used to read and write data.
	 */
	private final ByteBuf buf;
	
	/**
	 * Encrypts packet identifiers.
	 */
	private final IsaacRandom encryptor;
	
	/**
	 * The writer index where the size has been written for the current message being built.
	 */
	private int sizeIndex;
	
	/**
	 * The {@link PacketType} of the message currently being built.
	 */
	private PacketType msgType;
	
	/**
	 * The current bit position when writing bits.
	 */
	private int bitIndex = -1;
	
	/**
	 * Creates a new {@link GameBuffer}.
	 */
	public GameBuffer(ByteBuf buf) {
		this(buf, null);
	}
	
	/**
	 * Creates a new {@link GameBuffer}.
	 */
	public GameBuffer(ByteBuf buf, IsaacRandom encryptor) {
		this.buf = buf;
		this.encryptor = encryptor;
	}
	
	/**
	 * Creates a new {@link GameBuffer}.
	 */
	public GameBuffer(IsaacRandom encryptor) {
		this.buf = Unpooled.buffer(128);
		this.encryptor = encryptor;
	}
	
	/**
	 * Constructs a new fixed sized message.
	 * @param id The id of the message.
	 */
	public void message(int id) {
		message(id, PacketType.FIXED);
	}
	
	/**
	 * Constructs a new message.
	 * @param id   The id of the message.
	 * @param type The type of message to build.
	 */
	public void message(int id, PacketType type) {
		if(type == PacketType.RAW) {
			throw new IllegalArgumentException();
		}
		buf.writeByte(id + encryptor.nextInt());
		sizeIndex = buf.writerIndex();
		msgType = type;
		if(type == PacketType.VARIABLE_BYTE) {
			buf.writeByte(0);
		} else if(type == PacketType.VARIABLE_SHORT) {
			buf.writeShort(0);
		}
	}
	
	/**
	 * Ends the building of a message where the size may vary.
	 */
	public void endVarSize() {
		int recordedSize = buf.writerIndex() - sizeIndex;
		if(msgType == PacketType.VARIABLE_BYTE) {
			buf.setByte(sizeIndex, recordedSize - 1);
		} else if(msgType == PacketType.VARIABLE_SHORT) {
			buf.setShort(sizeIndex, recordedSize - 2);
		}
	}
	
	/**
	 * Prepares the buffer for writing bits.
	 */
	public void startBitAccess() {
		bitIndex = buf.writerIndex() << 3;
	}
	
	/**
	 * Prepares the buffer for writing bytes.
	 */
	public void endBitAccess() {
		buf.writerIndex((bitIndex + 7) >> 3);
		bitIndex = -1;
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
	 * @param from the argued buffer that bytes will be written from.
	 * @return an instance of this message builder.
	 */
	public void putBytes(byte[] from, int size) {
		buf.writeBytes(from, 0, size);
	}
	
	/**
	 * Writes the bytes from the argued buffer into this buffer. This method does not modify the argued buffer.
	 * @param from The argued buffer that bytes will be written from.
	 * @return An instance of this byte message.
	 */
	public void putBytes(GameBuffer from) {
		putBytes(from.getBuffer());
	}
	
	/**
	 * Writes the bytes from the argued buffer into this buffer.
	 * @param from The argued buffer that bytes will be written from.
	 * @return An instance of this byte message.
	 */
	public void putBytes(byte[] from) {
		buf.writeBytes(from, 0, from.length);
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
	 * @param value   The value.
	 * @throws IllegalArgumentException If the number of bits is not between 1 and 31 inclusive.
	 */
	public void putBits(int numBits, int value) {
		if(!buf.hasArray()) {
			throw new UnsupportedOperationException("The ByteBuf implementation must support array() for bit usage.");
		}
		
		int bytes = (int) Math.ceil((double) numBits / 8D) + 1;
		buf.ensureWritable((bitIndex + 7) / 8 + bytes);
		
		final byte[] buffer = this.buf.array();
		
		int bytePos = bitIndex >> 3;
		int bitOffset = 8 - (bitIndex & 7);
		bitIndex += numBits;
		
		for(; numBits > bitOffset; bitOffset = 8) {
			buffer[bytePos] &= ~BIT_MASK[bitOffset];
			buffer[bytePos++] |= (value >> (numBits - bitOffset)) & BIT_MASK[bitOffset];
			numBits -= bitOffset;
		}
		if(numBits == bitOffset) {
			buffer[bytePos] &= ~BIT_MASK[bitOffset];
			buffer[bytePos] |= value & BIT_MASK[bitOffset];
		} else {
			buffer[bytePos] &= ~(BIT_MASK[numBits] << (bitOffset - numBits));
			buffer[bytePos] |= (value & BIT_MASK[numBits]) << (bitOffset - numBits);
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
	 * @param type  The byte transformation type
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
		buf.writeByte((byte) value);
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
	 * @param type  The byte transformation type
	 * @param order The byte endianness type.
	 * @return An instance of this byte message.
	 * @throws UnsupportedOperationException If middle or inverse-middle value types are selected.
	 */
	public void putShort(int value, ByteTransform type, ByteOrder order) {
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
		putShort(value, ByteTransform.NORMAL, ByteOrder.BIG);
	}
	
	/**
	 * Writes a value as a big-endian {@code short}.
	 * @param value The value to write.
	 * @param type  The byte transformation type
	 * @return An instance of this byte message.
	 */
	public void putShort(int value, ByteTransform type) {
		putShort(value, type, ByteOrder.BIG);
	}
	
	/**
	 * Writes a value as a standard {@code short}.
	 * @param value The value to write.
	 * @param order The byte endianness type.
	 * @return An instance of this byte message.
	 */
	public void putShort(int value, ByteOrder order) {
		putShort(value, ByteTransform.NORMAL, order);
	}
	
	/**
	 * Writes a value as an {@code int}.
	 * @param value The value to write.
	 * @param type  The byte transformation type
	 * @param order The byte endianness type.
	 * @return An instance of this byte message.
	 */
	public void putInt(int value, ByteTransform type, ByteOrder order) {
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
		putInt(value, ByteTransform.NORMAL, ByteOrder.BIG);
	}
	
	/**
	 * Writes a value as a big-endian {@code int}.
	 * @param value The value to write.
	 * @param type  The byte transformation type
	 * @return An instance of this byte message.
	 */
	public void putInt(int value, ByteTransform type) {
		putInt(value, type, ByteOrder.BIG);
	}
	
	/**
	 * Writes a value as a standard {@code int}.
	 * @param value The value to write.
	 * @param order The byte endianness type.
	 * @return An instance of this byte message.
	 */
	public void putInt(int value, ByteOrder order) {
		putInt(value, ByteTransform.NORMAL, order);
	}
	
	/**
	 * Writes a value as a {@code long}.
	 * @param value The value to write.
	 * @param type  The byte transformation type
	 * @param order The byte endianness type.
	 * @return An instance of this byte message.
	 * @throws UnsupportedOperationException If middle or inverse-middle value types are selected.
	 */
	public void putLong(long value, ByteTransform type, ByteOrder order) {
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
		putLong(value, ByteTransform.NORMAL, ByteOrder.BIG);
	}
	
	/**
	 * Writes a value as a big-endian {@code long}.
	 * @param value The value to write.
	 * @param type  The byte transformation type
	 * @return An instance of this byte message.
	 */
	public void putLong(long value, ByteTransform type) {
		putLong(value, type, ByteOrder.BIG);
	}
	
	/**
	 * Writes a value as a standard {@code long}.
	 * @param value The value to write.
	 * @param order The byte endianness type. to write.
	 * @return An instance of this byte message.
	 */
	public void putLong(long value, ByteOrder order) {
		putLong(value, ByteTransform.NORMAL, order);
	}
	
	/**
	 * Writes a RuneScape {@code String} value.
	 * @param string The string to write.
	 * @return An instance of this byte message.
	 */
	public GameBuffer putCString(String string) {
		for(byte value : string.getBytes()) {
			put(value);
		}
		put(PacketUtils.TERMINATOR_VALUE);
		return this;
	}
	
	/**
	 * @return The internal buffer.
	 */
	public ByteBuf getBuffer() {
		return buf;
	}
	
	/**
	 * Releases the buffer.
	 */
	public void release() {
		buf.release();
	}
	
	/**
	 * Retains the internal buffer.
	 */
	public ByteBuf retain() {
		return buf.retain();
	}
	
	/**
	 * Clears the contents of this buffer.
	 */
	public void clear() {
		buf.clear();
	}
	
	public int count() {
		return buf.refCnt();
	}
}
