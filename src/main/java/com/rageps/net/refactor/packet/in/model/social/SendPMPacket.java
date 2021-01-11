package com.rageps.net.refactor.packet.in.model.social;

import com.rageps.net.refactor.packet.Packet;


public class SendPMPacket extends Packet {

    private final long to;

    private final String decoded;

    private final byte[] compressed;

    public SendPMPacket(long to, String decoded, byte[] compressed) {
        this.decoded = decoded;
        this.to = to;
        this.compressed = compressed;
    }

    public byte[] getCompressed() {
        return compressed;
    }

    public String getDecoded() {
        return decoded;
    }

    public long getTo() {
        return to;
    }
}