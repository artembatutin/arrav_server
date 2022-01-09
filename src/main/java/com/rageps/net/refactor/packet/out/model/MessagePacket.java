package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class MessagePacket extends Packet {

    private final String message;

    public MessagePacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}