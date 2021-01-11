package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class TradeRequestPacketPacket extends Packet {

    private final int index;

    public TradeRequestPacketPacket(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}