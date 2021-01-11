package com.rageps.net.refactor.packet.in.model.social;

import com.rageps.net.refactor.packet.Packet;


public class RemoveIgnorePacket extends Packet {

    private final long name;

    public RemoveIgnorePacket(long name) {
        this.name = name;
    }

    public long getName() {
        return name;
    }
}