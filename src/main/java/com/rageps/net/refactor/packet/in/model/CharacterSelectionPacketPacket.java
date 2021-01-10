package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * The message sent from the client when the character clicks "accept" on the
 * character selection interface.
 * @author Tamatea <tamateea@gmail.com>
 */
public class CharacterSelectionPacketPacket extends Packet {

    private final int[] values;

    public CharacterSelectionPacketPacket(int[] values) {
        this.values = values;
    }

    public int[] getValues() {
        return values;
    }
}