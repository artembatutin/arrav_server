package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class SlotPacket extends Packet {

    private final int slot;

    public SlotPacket(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }
}
