package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * The message sent from the client when the player clicks an item.
 * @author Tamatea <tamateea@gmail.com>
 */
public class ThirdItemActionPacket extends Packet {

    private final int container, slot, id;

    public ThirdItemActionPacket(int container, int slot, int id) {
        this.container = container;
        this.slot = slot;
        this.id = id;
    }

    public int getSlot() {
        return slot;
    }

    public int getId() {
        return id;
    }

    public int getContainer() {
        return container;
    }
}