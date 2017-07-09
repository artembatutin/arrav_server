package net.edge.net.codec;

import io.netty.buffer.ByteBuf;
import net.edge.net.packet.PacketHelper;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 5-7-2017.
 */
public final class GameBuffer {
    /**
     * The backing byte buffer used to read and write data.
     */
    private final ByteBuf buf;

    /**
     * Encrypts packet identifiers.
     */
    private final IsaacCipher encryptor;

    /**
     * The writer index where the size has been written for the current message being built.
     */
    private int sizeIndex;

    /**
     * The {@link MessageType} of the message currently being built.
     */
    private MessageType msgType;

    /**
     * The current bit position when writing bits.
     */
    private int bitIndex = -1;

    /*
     * A static initialization block that calculates the bit masks.
     */
    static {
        for(int i = 0; i < BitConstants.BIT_MASK.length; i++) {
            BitConstants.BIT_MASK[i] = (1 << i) - 1;
        }
    }

    /**
     * Creates a new {@link GameBuffer}.
     */
    public GameBuffer(ByteBuf buf) {
        this(buf, null);
    }

    /**
     * Creates a new {@link GameBuffer}.
     */
    public GameBuffer(ByteBuf buf, IsaacCipher encryptor) {
        this.buf = buf;
        this.encryptor = encryptor;
    }

    /**
     * Constructs a new fixed sized message.
     * @param id The id of the message.
     */
    public void message(int id) {
        message(id, MessageType.FIXED);
    }

    /**
     * Constructs a new message.
     * @param id The id of the message.
     * @param type The type of message to build.
     */
    public void message(int id, MessageType type) {
        if (type == MessageType.RAW) {
            throw new IllegalArgumentException();
        }
        buf.writeByte(id + encryptor.nextInt());
        sizeIndex = buf.writerIndex();
        msgType = type;
        if (type == MessageType.VARIABLE) {
            buf.writeByte(0);
        } else if (type == MessageType.VARIABLE_SHORT) {
            buf.writeShort(0);
        }
    }

    /**
     * Ends the building of a message where the size may vary.
     */
    public void endVarSize() {
        int recordedSize = buf.writerIndex() - sizeIndex;
        if (msgType == MessageType.VARIABLE) {
            buf.setByte(sizeIndex, recordedSize - 1);
        } else if (msgType == MessageType.VARIABLE_SHORT) {
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
     * Writes the bytes from the argued buffer into this buffer. This method does not modify the argued buffer.
     * @param from The argued buffer that bytes will be written from.
     * @return An instance of this byte message.
     */
    public GameBuffer putBytes(ByteBuf from) {
        for(int i = 0; i < from.writerIndex(); i++) {
            put(from.getByte(i));
        }
        return this;
    }

    /**
     * Writes the bytes from the argued buffer into this buffer.
     * @param from the argued buffer that bytes will be written from.
     * @return an instance of this message builder.
     */
    public GameBuffer putBytes(byte[] from, int size) {
        buf.writeBytes(from, 0, size);
        return this;
    }

    /**
     * Writes the bytes from the argued buffer into this buffer. This method does not modify the argued buffer.
     * @param from The argued buffer that bytes will be written from.
     * @return An instance of this byte message.
     */
    public GameBuffer putBytes(GameBuffer from) {
        return putBytes(from.getBuffer());
    }

    /**
     * Writes the bytes from the argued buffer into this buffer.
     * @param from The argued buffer that bytes will be written from.
     * @return An instance of this byte message.
     */
    public GameBuffer putBytes(byte[] from) {
        buf.writeBytes(from, 0, from.length);
        return this;
    }

    /**
     * Writes the bytes from the argued byte array into this buffer, in reverse.
     * @param data The data to write to this buffer.
     */
    public GameBuffer putBytesReverse(byte[] data) {
        for(int i = data.length - 1; i >= 0; i--) {
            put(data[i]);
        }
        return this;
    }

    /**
     * Writes the value as a variable amount of bits.
     * @param amount The amount of bits to write.
     * @param value  The value of the bits.
     * @return An instance of this byte message.
     * @throws IllegalArgumentException If the number of bits is not between {@code 1} and {@code 32} inclusive.
     */
    public GameBuffer putBits(int amount, int value) {
        int bytePos = bitIndex >> 3;
        int bitOffset = 8 - (bitIndex & 7);
        bitIndex = bitIndex + amount;
        int requiredSpace = bytePos - buf.writerIndex() + 1;
        requiredSpace += (amount + 7) / 8;
        if(buf.writableBytes() < requiredSpace) {
            buf.capacity(buf.capacity() + requiredSpace);
        }
        for(; amount > bitOffset; bitOffset = 8) {
            byte tmp = buf.getByte(bytePos);
            tmp &= ~BitConstants.BIT_MASK[bitOffset];
            tmp |= (value >> (amount - bitOffset)) & BitConstants.BIT_MASK[bitOffset];
            buf.setByte(bytePos++, tmp);
            amount -= bitOffset;
        }
        if(amount == bitOffset) {
            byte tmp = buf.getByte(bytePos);
            tmp &= ~BitConstants.BIT_MASK[bitOffset];
            tmp |= value & BitConstants.BIT_MASK[bitOffset];
            buf.setByte(bytePos, tmp);
        } else {
            byte tmp = buf.getByte(bytePos);
            tmp &= ~(BitConstants.BIT_MASK[amount] << (bitOffset - amount));
            tmp |= (value & BitConstants.BIT_MASK[amount]) << (bitOffset - amount);
            buf.setByte(bytePos, tmp);
        }
        return this;
    }

    /**
     * Writes a boolean bit flag.
     * @param flag The flag to write.
     * @return An instance of this byte message.
     */
    public GameBuffer putBit(boolean flag) {
        putBits(1, flag ? 1 : 0);
        return this;
    }

    /**
     * Writes a value as a {@code byte}.
     * @param value The value to write.
     * @param type  The byte transformation type
     * @return An instance of this byte message.
     */
    public GameBuffer put(int value, ByteTransform type) {
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
        return this;
    }

    /**
     * Writes a value as a normal {@code byte}.
     * @param value The value to write.
     * @return An instance of this byte message.
     */
    public GameBuffer put(int value) {
        put(value, ByteTransform.NORMAL);
        return this;
    }

    /**
     * Writes a value as a {@code short}.
     * @param value The value to write.
     * @param type  The byte transformation type
     * @param order The byte endianness type.
     * @return An instance of this byte message.
     * @throws UnsupportedOperationException If middle or inverse-middle value types are selected.
     */
    public GameBuffer putShort(int value, ByteTransform type, ByteOrder order) {
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
        return this;
    }

    /**
     * Writes a value as a normal big-endian {@code short}.
     * @param value The value to write.
     * @return An instance of this byte message.
     */
    public GameBuffer putShort(int value) {
        putShort(value, ByteTransform.NORMAL, ByteOrder.BIG);
        return this;
    }

    /**
     * Writes a value as a big-endian {@code short}.
     * @param value The value to write.
     * @param type  The byte transformation type
     * @return An instance of this byte message.
     */
    public GameBuffer putShort(int value, ByteTransform type) {
        putShort(value, type, ByteOrder.BIG);
        return this;
    }

    /**
     * Writes a value as a standard {@code short}.
     * @param value The value to write.
     * @param order The byte endianness type.
     * @return An instance of this byte message.
     */
    public GameBuffer putShort(int value, ByteOrder order) {
        putShort(value, ByteTransform.NORMAL, order);
        return this;
    }

    /**
     * Writes a value as an {@code int}.
     * @param value The value to write.
     * @param type  The byte transformation type
     * @param order The byte endianness type.
     * @return An instance of this byte message.
     */
    public GameBuffer putInt(int value, ByteTransform type, ByteOrder order) {
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
        return this;
    }

    /**
     * Writes a value as a standard big-endian {@code int}.
     * @param value The value to write.
     * @return An instance of this byte message.
     */
    public GameBuffer putInt(int value) {
        putInt(value, ByteTransform.NORMAL, ByteOrder.BIG);
        return this;
    }

    /**
     * Writes a value as a big-endian {@code int}.
     * @param value The value to write.
     * @param type  The byte transformation type
     * @return An instance of this byte message.
     */
    public GameBuffer putInt(int value, ByteTransform type) {
        putInt(value, type, ByteOrder.BIG);
        return this;
    }

    /**
     * Writes a value as a standard {@code int}.
     * @param value The value to write.
     * @param order The byte endianness type.
     * @return An instance of this byte message.
     */
    public GameBuffer putInt(int value, ByteOrder order) {
        putInt(value, ByteTransform.NORMAL, order);
        return this;
    }

    /**
     * Writes a value as a {@code long}.
     * @param value The value to write.
     * @param type  The byte transformation type
     * @param order The byte endianness type.
     * @return An instance of this byte message.
     * @throws UnsupportedOperationException If middle or inverse-middle value types are selected.
     */
    public GameBuffer putLong(long value, ByteTransform type, ByteOrder order) {
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
        return this;
    }

    /**
     * Writes a value as a standard big-endian {@code long}.
     * @param value The value to write.
     * @return An instance of this byte message.
     */
    public GameBuffer putLong(long value) {
        putLong(value, ByteTransform.NORMAL, ByteOrder.BIG);
        return this;
    }

    /**
     * Writes a value as a big-endian {@code long}.
     * @param value The value to write.
     * @param type  The byte transformation type
     * @return An instance of this byte message.
     */
    public GameBuffer putLong(long value, ByteTransform type) {
        putLong(value, type, ByteOrder.BIG);
        return this;
    }

    /**
     * Writes a value as a standard {@code long}.
     * @param value The value to write.
     * @param order The byte endianness type. to write.
     * @return An instance of this byte message.
     */
    public GameBuffer putLong(long value, ByteOrder order) {
        putLong(value, ByteTransform.NORMAL, order);
        return this;
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
        put(PacketHelper.TERMINATOR_VALUE);
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
