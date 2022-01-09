package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class InventoryInterfacePacket extends Packet {

    private final int open, overlay;

    public InventoryInterfacePacket(int open, int overlay) {
        this.open = open;
        this.overlay = overlay;
    }

    public int getOpen() {
        return open;
    }

    public int getOverlay() {
        return overlay;
    }
}