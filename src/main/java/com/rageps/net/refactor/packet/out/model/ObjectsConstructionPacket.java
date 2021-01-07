package com.rageps.net.refactor.packet.out.model;

import com.rageps.content.skill.construction.furniture.HotSpots;
import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ObjectsConstructionPacket extends Packet {

    private final HotSpots spot;

    private final Player player;

    public ObjectsConstructionPacket(Player player,HotSpots spot) {
        this.spot = spot;
        this.player = player;
    }

    public HotSpots getSpot() {
        return spot;
    }

    public Player getPlayer() {
        return player;
    }
}