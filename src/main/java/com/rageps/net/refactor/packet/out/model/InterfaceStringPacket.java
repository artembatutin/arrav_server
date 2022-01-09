package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class InterfaceStringPacket extends Packet {

    private final int id;
    private final String text;

    public InterfaceStringPacket(int id, String text) {
        this.id = id;
        this.text = text;
    }
    public InterfaceStringPacket(String text, int id) {
        this(id, text);
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}