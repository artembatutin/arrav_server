package com.rageps.net.refactor.packet.in.handler;

import com.rageps.net.refactor.packet.in.model.CharacterSelectionPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class CharacterSelectionPacketPacketHandler implements PacketHandler<CharacterSelectionPacketPacket> {

    @Override
    public void handle(Player player, CharacterSelectionPacketPacket packet) {
    }
}
