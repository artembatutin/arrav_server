package com.rageps.net.refactor.packet.in.handler;

import com.rageps.net.refactor.packet.in.model.MovementQueuePacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class MovementQueuePacketPacketHandler implements PacketHandler<MovementQueuePacketPacket> {

    @Override
    public void handle(Player player, MovementQueuePacketPacket packet) {
    }
}
