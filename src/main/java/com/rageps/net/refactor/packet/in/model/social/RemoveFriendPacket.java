package com.rageps.net.refactor.packet.in.model.social;

import com.rageps.net.refactor.packet.Packet;


public class RemoveFriendPacket extends Packet {

    private final long name;

    public RemoveFriendPacket(long name) {
        this.name = name;
    }

    public long getName() {
        return name;
    }
}