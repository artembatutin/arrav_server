package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class PickupItemPacketPacket extends Packet {


    private final int itemY, itemId, itemX;

    public PickupItemPacketPacket(int itemY, int itemId, int itemX) {
        this.itemY = itemY;
        this.itemId = itemId;
        this.itemX = itemX;
    }

    public int getItemId() {
        return itemId;
    }

    public int getItemY() {
        return itemY;
    }

    public int getItemX() {
        return itemX;
    }
}