package com.rageps.net.refactor.packet.in.model.social;

import com.rageps.net.refactor.packet.Packet;


public class AddFriendPacket extends Packet {

    private final long name;

    public AddFriendPacket(long name) {
        this.name = name;
    }

    public long getName() {
        return name;
    }
}