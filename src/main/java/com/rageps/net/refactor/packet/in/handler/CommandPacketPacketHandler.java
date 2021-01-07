package com.rageps.net.refactor.packet.in.handler;

import com.rageps.net.refactor.packet.in.model.CommandPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class CommandPacketPacketHandler implements PacketHandler<CommandPacketPacket> {

    @Override
    public void handle(Player player, CommandPacketPacket packet) {
    }
}
