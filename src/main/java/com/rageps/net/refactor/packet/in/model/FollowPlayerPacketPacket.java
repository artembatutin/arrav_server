package com.rageps.net.refactor.packet.in.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.actor.player.Player;

/**
 * The message sent from the client when a player tries to follow another player.
 * @author Tamatea <tamateea@gmail.com>
 */
public class FollowPlayerPacketPacket extends Packet {

    private final Player followed;

    public FollowPlayerPacketPacket(Player followed) {
        this.followed = followed;
    }

    public Player getFollowed() {
        return followed;
    }
}