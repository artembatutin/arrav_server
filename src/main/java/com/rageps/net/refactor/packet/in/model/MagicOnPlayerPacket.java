package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

public class MagicOnPlayerPacket extends Packet {

    private final int index;

    private final int spellID;

    public MagicOnPlayerPacket(int index, int spellID) {
        this.index = index;
        this.spellID = spellID;
    }

    public int getIndex() {
        return index;
    }

    public int getSpellID() {
        return spellID;
    }
}