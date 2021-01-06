package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class AddIgnorePacket extends Packet {

    private final long username;

    public AddIgnorePacket(long username) {
        this.username = username;
    }

    public long getUsername() {
        return username;
    }
}