package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemOnMobPacketPacket extends Packet {

    private final int itemId, mob, slot, container;

    public ItemOnMobPacketPacket(int itemId, int mob, int slot, int contianer) {
        this.itemId = itemId;
        this.mob = mob;
        this.slot = slot;
        this.container = contianer;
    }

    public int getItemId() {
        return itemId;
    }

    public int getSlot() {
        return slot;
    }

    public int getContainer() {
        return container;
    }

    public int getMob() {
        return mob;
    }
}