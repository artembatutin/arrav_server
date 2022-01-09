package com.rageps.net.refactor.packet.in.model.interface_actions;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class EquipItemPacket extends Packet {

    private final int interfaceID, slot, itemId;

    public EquipItemPacket(int interfaceID, int slot, int itemId) {
        this.interfaceID = interfaceID;
        this.slot = slot;
        this.itemId = itemId;
    }

    public int getSlot() {
        return slot;
    }

    public int getItemId() {
        return itemId;
    }

    public int getInterfaceID() {
        return interfaceID;
    }
}