package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class WalkablePacket extends Packet {

    private final int id;

    public WalkablePacket(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}