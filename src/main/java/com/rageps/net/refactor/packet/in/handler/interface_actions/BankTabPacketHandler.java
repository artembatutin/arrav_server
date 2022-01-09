package com.rageps.net.refactor.packet.in.handler.interface_actions;

import com.rageps.net.refactor.codec.game.DataOrder;
import com.rageps.net.refactor.codec.game.DataTransformation;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.in.model.interface_actions.BankTabPacket;
import com.rageps.net.refactor.packet.in.model.interface_actions.SwapSlotPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerAttributes;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.item.container.impl.Bank;

/**
 * todo - check verificiation if a player doesn't have item
 */
public class BankTabPacketHandler implements PacketHandler<BankTabPacket> {

    @Override
    public void handle(Player player, BankTabPacket packet) {

        int tab = packet.getTab();
        int fromSlot = packet.getFromSlot();
        int toTab = packet.getToTab();

        if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_INTERFACE))
            return;

        if(tab < 0 || fromSlot < 0 || toTab < 0)
            return;
        player.getBank().tabTransfer(tab, fromSlot, toTab);

        player.getActivityManager().execute(ActivityManager.ActivityType.ITEM_INTERFACE);
    }
}