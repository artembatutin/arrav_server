package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class PrivacyOptionPacketPacket extends Packet {

    private final int publicMode, privateMode, tradeMode, clanMode;

    public PrivacyOptionPacketPacket(int publicMode, int privateMode, int tradeMode, int clanMode) {
        this.privateMode= privateMode;
        this.publicMode = publicMode;
        this.tradeMode = tradeMode;
        this.clanMode = clanMode;
    }

    public int getClanMode() {
        return clanMode;
    }

    public int getPrivateMode() {
        return privateMode;
    }

    public int getPublicMode() {
        return publicMode;
    }

    public int getTradeMode() {
        return tradeMode;
    }
}