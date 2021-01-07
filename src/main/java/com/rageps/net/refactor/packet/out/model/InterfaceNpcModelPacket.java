package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class InterfaceNpcModelPacket extends Packet {

    private final int id, model;

    public InterfaceNpcModelPacket(int id, int model) {
        this.id = id;
        this.model = model;
    }

    public int getId() {
        return id;
    }

    public int getModel() {
        return model;
    }
}