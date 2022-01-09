package com.rageps.net.refactor.packet.in.handler;

import com.rageps.command.CommandDispatcher;
import com.rageps.net.refactor.packet.in.model.CommandPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;

/**
 * todo - replace with my command system from azerite
 * @author Tamatea <tamateea@gmail.com>
 */
public class CommandPacketPacketHandler implements PacketHandler<CommandPacketPacket> {

    @Override
    public void handle(Player player, CommandPacketPacket packet) {
        if(player.getActivityManager().contains(ActivityManager.ActivityType.COMMAND_MESSAGE)) {
            return;
        }
        String command = packet.getCommand();
        String[] parts = command.toLowerCase().split(" ");
        CommandDispatcher.execute(player, parts, command);
        player.getActivityManager().execute(ActivityManager.ActivityType.COMMAND_MESSAGE);
    }
}
