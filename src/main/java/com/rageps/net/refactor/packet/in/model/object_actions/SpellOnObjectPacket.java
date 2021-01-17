package com.rageps.net.refactor.packet.in.model.object_actions;

import com.rageps.net.refactor.packet.Packet;

public class SpellOnObjectPacket extends Packet {

    private final int objectId;

    private final int objectX;

    private final int objectY;

    private final int spellId;

    public SpellOnObjectPacket(int objectId, int objectX, int objectY, int spellId) {
        this.objectId = objectId;
        this.objectX = objectX;
        this.objectY = objectY;
        this.spellId = spellId;
    }

    public int getObjectY() {
        return objectY;
    }

    public int getObjectX() {
        return objectX;
    }

    public int getObjectId() {
        return objectId;
    }

    public int getSpellId() {
        return spellId;
    }
}