package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.actor.Actor;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ArrowEntityPacket extends Packet {

    private final Actor entity;

    public ArrowEntityPacket(Actor entity) {
        this.entity = entity;
    }

    public Actor getEntity() {
        return entity;
    }
}