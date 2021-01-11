package com.rageps.net.refactor.packet.in.model.social;

import com.rageps.net.refactor.packet.Packet;


public class AddIgnorePacket extends Packet {

    private final long name;

    public AddIgnorePacket(long name) {
        this.name = name;
    }

    public long getName() {
        return name;
    }
}