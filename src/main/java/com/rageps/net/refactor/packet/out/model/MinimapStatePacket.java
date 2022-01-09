package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class MinimapStatePacket extends Packet {

    private final int code;

    public MinimapStatePacket(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}