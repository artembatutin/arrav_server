package com.rageps.net.refactor.packet.in.handler;

import com.rageps.net.refactor.packet.in.model.IdleStatePacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class IdleStatePacketPacketHandler implements PacketHandler<IdleStatePacketPacket> {

    @Override
    public void handle(Player player, IdleStatePacketPacket packet) {
    }
}
