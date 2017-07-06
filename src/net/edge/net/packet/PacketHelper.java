package net.edge.net.packet;

import io.netty.buffer.ByteBuf;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 5-7-2017.
 */
public final class PacketHelper {
    /**
     * The C-String terminator value.
     */
    public static final int TERMINATOR_VALUE = 10;

    /**
     * Reads a series of byte data terminated by a null value, casted to a {@link String}.
     */
    public static String getCString(ByteBuf buf) {
        byte temp;
        StringBuilder b = new StringBuilder();
        while(buf.isReadable() && (temp = (byte) buf.readUnsignedByte()) != TERMINATOR_VALUE) {
            b.append((char) temp);
        }
        return b.toString();
    }

    public static void writeCString(ByteBuf buf, String value) {
        for (int i = 0; i < value.length(); i++) {
            buf.writeByte(value.charAt(i));
        }

        buf.writeByte(TERMINATOR_VALUE);
    }

    /**
     * Prevents external instantiation.
     */
    private PacketHelper() {
        // nothing
    }
}
