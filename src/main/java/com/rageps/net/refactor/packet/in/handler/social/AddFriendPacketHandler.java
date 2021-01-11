package com.rageps.net.refactor.packet.in.handler.social;

import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.in.model.AdvanceDialoguePacketPacket;
import com.rageps.net.refactor.packet.in.model.social.AddFriendPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class AddFriendPacketHandler implements PacketHandler<AddFriendPacket> {

    @Override
    public void handle(Player player, AddFriendPacket packet) {
        long name = packet.getName();
        if(name < 0)
            return;
        player.relations.addFriend(name);
    }
}
