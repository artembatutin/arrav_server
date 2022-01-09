package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class BroadcastPacket extends Packet {


    private final int id;
    private final int time;
    private final String context;

    public BroadcastPacket(int id, int time, String context) {
        this.id = id;
        this.time = time;
        this.context = context;
    }

    public BroadcastPacket(int id, String context) {
        this.id = id;
        this.time = -1;
        this.context = context;
    }

    public int getId() {
        return id;
    }

    public int getTime() {
        return time;
    }

    public String getContext() {
        return context;
    }
}