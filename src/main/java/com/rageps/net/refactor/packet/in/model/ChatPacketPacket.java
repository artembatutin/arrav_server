package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * The message sent from the client when the player speaks.
 * @author Tamatea <tamateea@gmail.com>
 */
public class ChatPacketPacket extends Packet {


    private final int effects;
    private final int color;
    private final byte[] text;

    public ChatPacketPacket(int effect, int color, byte[] text) {
        this.effects = effect;
        this.color = color;
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public byte[] getText() {
        return text;
    }

    public int getEffects() {
        return effects;
    }
}