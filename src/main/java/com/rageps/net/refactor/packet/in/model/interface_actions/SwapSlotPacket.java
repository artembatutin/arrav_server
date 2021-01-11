package com.rageps.net.refactor.packet.in.model.interface_actions;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class SwapSlotPacket extends Packet {

    private final int interfaceID, fromSlot, toSlot;

    public SwapSlotPacket(int interfaceID, int fromSlot, int toSlot) {
        this.interfaceID = interfaceID;
        this.fromSlot = fromSlot;
        this.toSlot = toSlot;
    }

    public int getFromSlot() {
        return fromSlot;
    }

    public int getToSlot() {
        return toSlot;
    }

    public int getInterfaceID() {
        return interfaceID;
    }
}