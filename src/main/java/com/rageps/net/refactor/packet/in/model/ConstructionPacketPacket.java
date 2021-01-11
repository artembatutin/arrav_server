package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ConstructionPacketPacket extends Packet {

    private final int click;

    public ConstructionPacketPacket(int click) {
        this.click = click;
    }

    public int getClick() {
        return click;
    }
}