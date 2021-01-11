package com.rageps.net.refactor.packet.in.handler;

import com.rageps.net.refactor.packet.in.model.ChatPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.actor.update.UpdateFlag;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class ChatPacketPacketHandler implements PacketHandler<ChatPacketPacket> {

    @Override
    public void handle(Player player, ChatPacketPacket packet) {
        if(player.getActivityManager().contains(ActivityManager.ActivityType.CHAT_MESSAGE))
            return;
        if(player.muted || player.ipMuted) {
            player.message("You are currently muted.");
            return;
        }
        player.setChatEffects(packet.getEffects());
        player.setChatColor(packet.getColor());
        player.setChatText(packet.getText());
        player.getFlags().flag(UpdateFlag.CHAT);
        player.getActivityManager().execute(ActivityManager.ActivityType.CHAT_MESSAGE);
    }
}
