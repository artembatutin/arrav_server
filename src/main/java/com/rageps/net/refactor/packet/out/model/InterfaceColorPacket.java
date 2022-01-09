package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class InterfaceColorPacket extends Packet {

    private final int id, color;

    public InterfaceColorPacket(int id, int color) {
        this.id = id;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public int getColor() {
        return color;
    }
}