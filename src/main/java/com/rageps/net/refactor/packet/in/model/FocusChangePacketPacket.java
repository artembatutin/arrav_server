package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * This message sent from the client when the focus of the player's window changes.
 * @author Tamatea <tamateea@gmail.com>
 */
public class FocusChangePacketPacket extends Packet {

    private final boolean focused;

    public FocusChangePacketPacket(boolean focused) {
        this.focused = focused;
    }

    public boolean isFocused() {
        return focused;
    }
}