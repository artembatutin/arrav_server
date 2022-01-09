package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.actor.player.Player;
import it.unimi.dsi.fastutil.objects.ObjectList;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class WildernessActivityPacket extends Packet {

    private final ObjectList<Player> pkers;
    private int fools;

    public WildernessActivityPacket(ObjectList<Player> pkers, int fools) {
        this.pkers = pkers;
        this.fools = fools;
    }

    public int getFools() {
        return fools;
    }

    public ObjectList<Player> getPkers() {
        return pkers;
    }
}