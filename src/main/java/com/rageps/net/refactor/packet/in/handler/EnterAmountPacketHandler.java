package com.rageps.net.refactor.packet.in.handler;

import com.rageps.net.refactor.packet.in.model.EnterAmountPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;

import java.util.Optional;

/**
 * The enter amount packet which deals with numerical values.
 * @author Tamatea <tamateea@gmail.com>
 */
public class EnterAmountPacketHandler implements PacketHandler<EnterAmountPacketPacket> {

    @Override
    public void handle(Player player, EnterAmountPacketPacket packet) {
        if(player.getActivityManager().contains(ActivityManager.ActivityType.ENTER_INPUT)) {
            return;
        }

        int amount = packet.getAmount();
        if(amount < 1) {
            return;
        }
        player.getEnterInputListener().ifPresent(t -> t.apply(Integer.toString(amount)).execute());
        player.setEnterInputListener(Optional.empty());

        player.getActivityManager().execute(ActivityManager.ActivityType.ENTER_INPUT);
    }
}
