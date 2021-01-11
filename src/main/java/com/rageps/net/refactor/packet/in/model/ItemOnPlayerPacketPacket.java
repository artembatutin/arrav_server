package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemOnPlayerPacketPacket extends Packet {

    private final int container, index, itemUsed, itemSlot;

    public ItemOnPlayerPacketPacket(int container, int index, int itemUsed, int itemSlot) {
        this.container = container;
        this.index = index;
        this.itemUsed = itemUsed;
        this.itemSlot = itemSlot;
    }

    public int getContainer() {
        return container;
    }

    public int getIndex() {
        return index;
    }

    public int getItemSlot() {
        return itemSlot;
    }

    public int getItemUsed() {
        return itemUsed;
    }
}