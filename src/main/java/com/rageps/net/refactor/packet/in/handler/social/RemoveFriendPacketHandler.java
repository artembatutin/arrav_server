package com.rageps.net.refactor.packet.in.handler.social;

import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.in.model.social.AddFriendPacket;
import com.rageps.net.refactor.packet.in.model.social.RemoveFriendPacket;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class RemoveFriendPacketHandler implements PacketHandler<RemoveFriendPacket> {

    @Override
    public void handle(Player player, RemoveFriendPacket packet) {
        long name = packet.getName();
        if(name < 0)
            return;
        player.relations.deleteFriend(name);
    }
}
