package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class MarketPacketPacket extends Packet {

    private final String text;

    public MarketPacketPacket(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}