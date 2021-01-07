package com.rageps.net.refactor.packet.in.handler;

import com.rageps.net.refactor.packet.in.model.UpdateRegionPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class UpdateRegionPacketPacketHandler implements PacketHandler<UpdateRegionPacketPacket> {

    @Override
    public void handle(Player player, UpdateRegionPacketPacket packet) {
    }
}
