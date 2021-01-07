package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class FadePacket extends Packet {

    private final int start, duration, end;

    public FadePacket(int start, int duration, int end) {
        this.start = start;
        this.duration = duration;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getDuration() {
        return duration;
    }

    public int getEnd() {
        return end;
    }
}