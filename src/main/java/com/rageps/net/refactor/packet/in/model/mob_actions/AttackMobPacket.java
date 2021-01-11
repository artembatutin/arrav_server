package com.rageps.net.refactor.packet.in.model.mob_actions;

import com.rageps.net.refactor.packet.Packet;

public class AttackMobPacket extends Packet {

    private final int index;

    public AttackMobPacket(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
