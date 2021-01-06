package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

public class InterfaceStringPacket extends Packet {

    private String message;

    private int interfaceId;

    public InterfaceStringPacket(int interfaceId, String message) {
        this.interfaceId = interfaceId;
        this.message = message;
    }

    public int getInterfaceId() {
        return interfaceId;
    }

    public String getMessage() {
        return message;
    }
}
