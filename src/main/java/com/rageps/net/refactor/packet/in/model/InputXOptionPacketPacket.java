package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class InputXOptionPacketPacket extends Packet {

    private final int slot, interfaceId, itemId;

    public InputXOptionPacketPacket(int slot, int interfaceId, int itemId) {
        this.slot = slot;
        this.interfaceId = interfaceId;
        this.itemId = itemId;
    }

    public int getSlot() {
        return slot;
    }

    public int getItemId() {
        return itemId;
    }

    public int getInterfaceId() {
        return interfaceId;
    }
}