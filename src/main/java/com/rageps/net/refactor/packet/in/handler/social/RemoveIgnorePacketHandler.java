package com.rageps.net.refactor.packet.in.handler.social;

import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.in.model.social.RemoveFriendPacket;
import com.rageps.net.refactor.packet.in.model.social.RemoveIgnorePacket;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class RemoveIgnorePacketHandler implements PacketHandler<RemoveIgnorePacket> {

    @Override
    public void handle(Player player, RemoveIgnorePacket packet) {
        long name = packet.getName();
        if(name < 0)
            return;
        player.relations.deleteIgnore(name);
    }
}
