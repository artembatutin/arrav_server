package com.rageps.net.refactor.packet.in.handler.interface_actions;

import com.rageps.action.impl.ItemAction;
import com.rageps.content.Attributes;
import com.rageps.content.market.MarketShop;
import com.rageps.content.skill.crafting.JewelleryMoulding;
import com.rageps.content.skill.runecrafting.Runecrafting;
import com.rageps.content.skill.runecrafting.pouch.PouchType;
import com.rageps.content.skill.smithing.Smithing;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.in.model.interface_actions.EquipItemPacket;
import com.rageps.net.refactor.packet.in.model.interface_actions.FirstItemInterfacePacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.container.session.ExchangeSession;
import com.rageps.world.entity.item.container.session.ExchangeSessionManager;
import com.rageps.world.entity.item.container.session.ExchangeSessionType;

import java.util.Optional;

import static com.rageps.action.ActionContainers.EQUIP;

/**
 * todo - check verificiation if a player doesn't have item
 */
public class EquipItemPacketHandler implements PacketHandler<EquipItemPacket> {

    @Override
    public void handle(Player player, EquipItemPacket packet) {
        int interfaceId = packet.getInterfaceID();
        int slot = packet.getSlot();
        int itemId = packet.getItemId();

        if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_INTERFACE))
            return;

        if(interfaceId < 0 || slot < 0 || itemId < 0) {
            return;
        }

        Item item = player.getInventory().get(slot);
        if(item == null || !Item.valid(item)) {
            return;
        }
        switch(itemId) {
            case 5509:
                Runecrafting.empty(player, PouchType.SMALL);
                return;

            case 5510:
                Runecrafting.empty(player, PouchType.MEDIUM);
                return;

            case 5512:
                Runecrafting.empty(player, PouchType.LARGE);
                return;

            case 5514:
                Runecrafting.empty(player, PouchType.GIANT);
                return;
        }
        ItemAction e = EQUIP.get(item.getId());
        if(e != null)
            if(e.click(player, item, interfaceId, slot, 5))
                return;
        player.getEquipment().equip(slot);

        player.getActivityManager().execute(ActivityManager.ActivityType.ITEM_INTERFACE);
    }
}