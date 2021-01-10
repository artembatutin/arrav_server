package com.rageps.net.refactor.packet.in.handler;

import com.rageps.net.refactor.packet.in.model.FollowPlayerPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class FollowPlayerPacketPacketHandler implements PacketHandler<FollowPlayerPacketPacket> {

    @Override
    public void handle(Player player, FollowPlayerPacketPacket packet) {

        Player follow = packet.getFollowed();

        if(follow == null || !follow.getPosition().isViewableFrom(player.getPosition()) || follow.same(player))
            return;

        player.getMovementQueue().follow(follow);
        player.getActivityManager().execute(ActivityManager.ActivityType.FOLLOW_PLAYER);


    }
}
