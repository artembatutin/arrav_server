package com.rageps.net.refactor.packet.in.handler;

import com.rageps.net.refactor.packet.in.model.ItemOnPlayerPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemOnPlayerPacketPacketHandler implements PacketHandler<ItemOnPlayerPacketPacket> {

    @Override
    public void handle(Player player, ItemOnPlayerPacketPacket packet) {
    }
}
