package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class InterfaceActionPacketPacket extends Packet {

    private final int interfaceId, action;

    public InterfaceActionPacketPacket(int interfaceId, int action) {
        this.interfaceId = interfaceId;
        this.action = action;
    }

    public int getInterfaceId() {
        return interfaceId;
    }

    public int getAction() {
        return action;
    }
}