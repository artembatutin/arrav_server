package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemOnObjectPacketPacket extends Packet {

    private final int container, objectId, objectY, slot, objectX, itemId;

    public ItemOnObjectPacketPacket(int container, int objectId, int objectY, int slot, int objectX, int itemId) {
        this.container = container;
        this.objectId = objectId;
        this.objectY = objectY;
        this.objectX = objectX;
        this.slot = slot;
        this.itemId = itemId;
    }

    public int getContainer() {
        return container;
    }

    public int getSlot() {
        return slot;
    }

    public int getItemId() {
        return itemId;
    }

    public int getObjectId() {
        return objectId;
    }

    public int getObjectX() {
        return objectX;
    }

    public int getObjectY() {
        return objectY;
    }
}