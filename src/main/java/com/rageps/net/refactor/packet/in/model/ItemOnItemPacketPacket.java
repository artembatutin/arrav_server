package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemOnItemPacketPacket extends Packet {

    private final int firstSlot, secondSlot;

    public ItemOnItemPacketPacket(int firstSlot, int secondSlot) {
        this.firstSlot = firstSlot;
        this.secondSlot = secondSlot;
    }

    public int getFirstSlot() {
        return firstSlot;
    }

    public int getSecondSlot() {
        return secondSlot;
    }
}