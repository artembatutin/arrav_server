package com.rageps.net.refactor.packet.in.handler.social;

import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.in.model.social.AddFriendPacket;
import com.rageps.net.refactor.packet.in.model.social.AddIgnorePacket;
import com.rageps.world.entity.actor.player.Player;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class AddIgnorePacketHandler implements PacketHandler<AddIgnorePacket> {

    @Override
    public void handle(Player player, AddIgnorePacket packet) {
        long name = packet.getName();
        if(name < 0)
            return;
        player.relations.addIgnore(name);
    }
}
