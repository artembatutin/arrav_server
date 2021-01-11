package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ClearTextPacket extends Packet {

    private final int start, count;
    private final Player player;

    public ClearTextPacket(Player player, int start, int count) {
        this.start = start;
        this.count = count;
        this.player = player;
    }

    public int getCount() {
        return count;
    }

    public Player getPlayer() {
        return player;
    }

    public int getStart() {
        return start;
    }
}