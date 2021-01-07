package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ObjectRemovalPacket extends Packet {

    private final GameObject object;
    private final Player player;

    public ObjectRemovalPacket(Player player, GameObject object) {
        this.object = object;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public GameObject getObject() {
        return object;
    }
}