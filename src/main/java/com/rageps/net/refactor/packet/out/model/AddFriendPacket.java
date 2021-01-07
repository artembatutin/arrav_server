package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class AddFriendPacket extends Packet {


    private final long username;
    private int world;
    private boolean display;

    public AddFriendPacket(long username, int world, boolean display) {
        this.username = username;
        this.world = world;
        this.display = display;
    }
    public AddFriendPacket(long username, int world) {
        this(username, world, true);
    }

    public int getWorld() {
        return world;
    }

    public long getUsername() {
        return username;
    }

    public boolean isDisplay() {
        return display;
    }
}