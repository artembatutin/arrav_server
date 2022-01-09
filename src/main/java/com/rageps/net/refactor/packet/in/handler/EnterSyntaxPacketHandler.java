package com.rageps.net.refactor.packet.in.handler;

import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.in.model.EnterAmountPacketPacket;
import com.rageps.net.refactor.packet.in.model.EnterSyntaxPacketPacket;
import com.rageps.net.refactor.packet.out.model.EnterAmountPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;

import java.util.Optional;

/**
 * The enter amount packet which deals with alphabetical values.
 * @author Tamatea <tamateea@gmail.com>
 */
public class EnterSyntaxPacketHandler implements PacketHandler<EnterSyntaxPacketPacket> {

    @Override
    public void handle(Player player, EnterSyntaxPacketPacket packet) {
        if(player.getActivityManager().contains(ActivityManager.ActivityType.ENTER_INPUT)) {
            return;
        }

        String text = packet.getText();

        if(text.isEmpty()) {
            return;
        }
        player.getEnterInputListener().ifPresent(t -> t.apply(text).execute());
        player.setEnterInputListener(Optional.empty());

        player.getActivityManager().execute(ActivityManager.ActivityType.ENTER_INPUT);
    }
}
