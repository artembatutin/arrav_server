package com.rageps.net.refactor.packet.in.handler;

import com.rageps.net.refactor.packet.in.model.ItemOnObjectPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ItemOnObjectPacketPacketHandler implements PacketHandler<ItemOnObjectPacketPacket> {

    @Override
    public void handle(Player player, ItemOnObjectPacketPacket packet) {
    }
}
