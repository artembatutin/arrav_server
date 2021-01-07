package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class FeedMessagePacket extends Packet {


    private final String message;
    private final String color;

    public FeedMessagePacket(String message, String color) {
        this.message = message;
        this.color = color;
    }

    public String getMessage() {
        return message;
    }

    public String getColor() {
        return color;
    }
}