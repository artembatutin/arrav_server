package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * The message sent from the client when the player clicks some sort of button or
 * module.
 * @author Tamatea <tamateea@gmail.com>
 */
public class ClickButtonPacketPacket extends Packet {

    private final int id;

    public ClickButtonPacketPacket(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}