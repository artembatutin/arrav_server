package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class InterfaceLayerPacket extends Packet {

    private final int id;
    private final boolean hide;

    public InterfaceLayerPacket(int id, boolean hide) {
        this.id = id;
        this.hide = hide;
    }

    public int getId() {
        return id;
    }

    public boolean isHide() {
        return hide;
    }
}