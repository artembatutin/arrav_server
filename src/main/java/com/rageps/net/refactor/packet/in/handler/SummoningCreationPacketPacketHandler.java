package com.rageps.net.refactor.packet.in.handler;

import com.rageps.net.refactor.packet.in.model.SummoningCreationPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class SummoningCreationPacketPacketHandler implements PacketHandler<SummoningCreationPacketPacket> {

    @Override
    public void handle(Player player, SummoningCreationPacketPacket packet) {
    }
}
