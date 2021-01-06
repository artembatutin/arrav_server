package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

public class TextMessagePacket extends Packet {

    private String message;

    public TextMessagePacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
