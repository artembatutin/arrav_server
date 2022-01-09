package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.actor.player.Player;

/**
 * The message sent from the client when a player attacks another player.
 * @author Tamatea <tamateea@gmail.com>
 */
public class AttackPlayerPacketPacket extends Packet {

    private final Player victim;

    public AttackPlayerPacketPacket(Player victim) {
        this.victim = victim;
    }

    public Player getVictim() {
        return victim;
    }
}