package com.rageps.net.refactor.packet.in.handler;

import com.rageps.content.minigame.MinigameHandler;
import com.rageps.net.refactor.packet.in.model.InterfaceClickPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.item.container.session.ExchangeSessionManager;

/**
 * The message sent from the client when a player clicks certain options on an
 * interface.
 * @author Tamatea <tamateea@gmail.com>
 */
public class InterfaceClickPacketPacketHandler implements PacketHandler<InterfaceClickPacketPacket> {

    @Override
    public void handle(Player player, InterfaceClickPacketPacket packet) {
        if(player.getActivityManager().contains(ActivityManager.ActivityType.INTERFACE_CLICK))
            return;

        if(ExchangeSessionManager.get().reset(player)) {
            return;
        }

        MinigameHandler.executeVoid(player, t -> t.onInterfaceClick(player));
        player.closeWidget();
        player.getActivityManager().execute(ActivityManager.ActivityType.INTERFACE_CLICK);
    }
}
