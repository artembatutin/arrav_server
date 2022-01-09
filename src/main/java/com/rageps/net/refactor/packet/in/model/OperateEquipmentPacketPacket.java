package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class OperateEquipmentPacketPacket extends Packet {

    private final int slot, option;

    public OperateEquipmentPacketPacket(int option, int slot) {
        this.slot = slot;
        this.option = option;
    }

    public int getSlot() {
        return slot;
    }

    public int getOption() {
        return option;
    }
}