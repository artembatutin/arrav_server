package com.rageps.net.refactor.packet.in.handler;

import com.rageps.net.refactor.packet.in.model.AttackPlayerPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class AttackPlayerPacketPacketHandler implements PacketHandler<AttackPlayerPacketPacket> {

    @Override
    public void handle(Player player, AttackPlayerPacketPacket packet) {
    }
}
