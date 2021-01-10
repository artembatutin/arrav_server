package com.rageps.net.refactor.packet.in.handler;

import com.rageps.net.refactor.packet.in.model.FocusChangePacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class FocusChangePacketPacketHandler implements PacketHandler<FocusChangePacketPacket> {

    @Override
    public void handle(Player player, FocusChangePacketPacket packet) {
        player.screenFocus = packet.isFocused();
    }
}
