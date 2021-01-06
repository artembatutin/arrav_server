package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.util.ActionListener;
import com.rageps.world.entity.actor.player.Player;

import java.util.function.Function;

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