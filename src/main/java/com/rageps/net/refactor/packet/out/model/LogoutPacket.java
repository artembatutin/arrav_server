package com.rageps.net.refactor.packet.out.model;

import com.rageps.net.refactor.packet.Packet;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class LogoutPacket extends Packet {

    private final Player player;

    public LogoutPacket(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}