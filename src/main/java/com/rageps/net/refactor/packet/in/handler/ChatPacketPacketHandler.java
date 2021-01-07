package com.rageps.net.refactor.packet.in.handler;

import com.rageps.net.refactor.packet.in.model.ChatPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ChatPacketPacketHandler implements PacketHandler<ChatPacketPacket> {

    @Override
    public void handle(Player player, ChatPacketPacket packet) {
    }
}
