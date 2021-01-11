package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class MagicOnItemPacketPacket extends Packet {

    private final int slot, id, interfaceId, spellId;

    public MagicOnItemPacketPacket(int slot, int id, int interfaceId, int spellId) {
        this.slot = slot;
        this.id = id;
        this.interfaceId = interfaceId;
        this.spellId = spellId;
    }

    public int getSpellId() {
        return spellId;
    }

    public int getSlot() {
        return slot;
    }

    public int getId() {
        return id;
    }

    public int getInterfaceId() {
        return interfaceId;
    }
}