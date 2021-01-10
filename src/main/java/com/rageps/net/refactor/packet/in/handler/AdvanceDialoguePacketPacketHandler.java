package com.rageps.net.refactor.packet.in.handler;

import com.rageps.net.refactor.packet.in.model.AdvanceDialoguePacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class AdvanceDialoguePacketPacketHandler implements PacketHandler<AdvanceDialoguePacketPacket> {

    @Override
    public void handle(Player player, AdvanceDialoguePacketPacket packet) {
        if(player.getActivityManager().contains(ActivityManager.ActivityType.DIALOGUE_INTERACTION))
            return;
        player.getDialogueBuilder().advance();
        player.getActivityManager().execute(ActivityManager.ActivityType.DIALOGUE_INTERACTION);
    }
}
