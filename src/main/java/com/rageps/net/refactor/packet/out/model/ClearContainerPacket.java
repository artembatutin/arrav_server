package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ClearContainerPacket extends Packet {

    private final int id;

    public ClearContainerPacket(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}