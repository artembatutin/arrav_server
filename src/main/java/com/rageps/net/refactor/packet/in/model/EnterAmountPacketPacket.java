package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class EnterAmountPacketPacket extends Packet {

    private final int amount;

    public EnterAmountPacketPacket(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}