package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class TooltipPacket extends Packet {

    private final String string;
    private final int id;

    public TooltipPacket(String string, int id) {
        this.string = string;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getString() {
        return string;
    }
}