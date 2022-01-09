package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class PrivateMessagePacket extends Packet {

    private final long name;
    private final int rights, size, privateMessageId;
    private final byte[] message;

    public PrivateMessagePacket(long name, int rights, byte[] message, int size, int privateMessageId) {
        this.name = name;
        this.rights = rights;
        this.message = message;
        this.size = size;
        this.privateMessageId = privateMessageId;
    }

    public int getPrivateMessageId() {
        return privateMessageId;
    }

    public byte[] getMessage() {
        return message;
    }

    public int getRights() {
        return rights;
    }

    public int getSize() {
        return size;
    }

    public long getName() {
        return name;
    }
}