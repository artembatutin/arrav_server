package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class EnterSyntaxPacketPacket extends Packet {

    private final String text;
    
    public EnterSyntaxPacketPacket(String amount) {
        this.text = amount;
    }

    public String getText() {
        return text;
    }
}