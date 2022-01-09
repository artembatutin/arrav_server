package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.actor.Actor;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class CombatTargetPacket extends Packet {


    private final Actor opponent;

    public CombatTargetPacket(Actor opponent) {
        this.opponent = opponent;
    }


    public Actor getOpponent() {
        return opponent;
    }
}