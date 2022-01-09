package com.rageps.net.refactor.packet.in.handler.interface_actions;

import com.rageps.action.impl.ItemAction;
import com.rageps.content.skill.runecrafting.Runecrafting;
import com.rageps.content.skill.runecrafting.pouch.PouchType;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.in.model.interface_actions.FirstItemInterfacePacket;
import com.rageps.net.refactor.packet.in.model.interface_actions.SwapSlotPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerAttributes;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.container.impl.Bank;

import static com.rageps.action.ActionContainers.EQUIP;

/**
 * todo - check verificiation if a player doesn't have item
 */
public class SwapSlotPacketHandler implements PacketHandler<SwapSlotPacket> {

    @Override
    public void handle(Player player, SwapSlotPacket packet) {
        int interfaceId = packet.getInterfaceID();
        int fromSlot = packet.getFromSlot();
        int toSlot = packet.getToSlot();

        if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_INTERFACE))
            return;

        if(interfaceId < 0 || fromSlot < 0 || toSlot < 0)
            return;


        switch(interfaceId) {
            case 3214://inventory
                player.getInventory().swap(fromSlot, toSlot);
                break;
            case Bank.BANK_INVENTORY_ID:
                if(player.getAttributeMap().getBoolean(PlayerAttributes.BANKING)) {

                    if(player.getAttributeMap().getBoolean(PlayerAttributes.INSERT_ITEM)) {
                        player.getBank().swap(0, fromSlot, toSlot);
                    } else {
                        player.getBank().transfer(0, fromSlot, toSlot);
                    }
                }
                break;
            case Bank.SIDEBAR_INVENTORY_ID://banking inventory
                if(player.getAttributeMap().getBoolean(PlayerAttributes.BANKING)) {
                    player.getInventory().swap(fromSlot, toSlot);
                    player.getInventory().refreshSingle(player, Bank.SIDEBAR_INVENTORY_ID, fromSlot);
                    player.getInventory().refreshSingle(player, Bank.SIDEBAR_INVENTORY_ID, toSlot);
                }
                break;
        }


        player.getActivityManager().execute(ActivityManager.ActivityType.ITEM_INTERFACE);
    }
}