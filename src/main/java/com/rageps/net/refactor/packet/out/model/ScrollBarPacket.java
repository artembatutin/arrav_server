package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ScrollBarPacket extends Packet {

    private final int id;
    private final int size;

    public ScrollBarPacket(int id, int size) {
        this.id = id;
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public int getId() {
        return id;
    }
}