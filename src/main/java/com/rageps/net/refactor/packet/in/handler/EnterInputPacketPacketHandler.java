package com.rageps.net.refactor.packet.in.handler;

import com.rageps.net.refactor.packet.in.model.EnterInputPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class EnterInputPacketPacketHandler implements PacketHandler<EnterInputPacketPacket> {

    @Override
    public void handle(Player player, EnterInputPacketPacket packet) {
    }
}
