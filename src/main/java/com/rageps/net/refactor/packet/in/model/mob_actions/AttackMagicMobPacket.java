package com.rageps.net.refactor.packet.in.model.mob_actions;

import com.rageps.net.refactor.packet.Packet;

public class AttackMagicMobPacket extends Packet {

    private final int index, spellId;

    public AttackMagicMobPacket(int index, int spellId) {
        this.index = index;
        this.spellId = spellId;
    }

    public int getSpellId() {
        return spellId;
    }

    public int getIndex() {
        return index;
    }
}
