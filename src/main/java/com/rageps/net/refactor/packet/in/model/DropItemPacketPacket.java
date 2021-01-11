package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * The message sent from the client when the player drops an item.
 * @author Tamatea <tamateea@gmail.com>
 */
public class DropItemPacketPacket extends Packet {

    private final int id;

    private final int slot;

    public DropItemPacketPacket(int id, int slot) {
        this.id = id;
        this.slot = slot;
    }

    public int getId() {
        return id;
    }

    public int getSlot() {
        return slot;
    }
}