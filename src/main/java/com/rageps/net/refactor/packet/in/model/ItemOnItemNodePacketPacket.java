package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemOnItemNodePacketPacket extends Packet {

    private final int itemID, itemX, itemY;

    public ItemOnItemNodePacketPacket(int itemX, int itemY, int itemID) {
        this.itemID = itemID;
        this.itemX = itemX;
        this.itemY = itemY;
    }

    public int getItemID() {
        return itemID;
    }

    public int getItemX() {
        return itemX;
    }

    public int getItemY() {
        return itemY;
    }
}