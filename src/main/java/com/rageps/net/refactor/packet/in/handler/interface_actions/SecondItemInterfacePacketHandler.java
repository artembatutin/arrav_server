package com.rageps.net.refactor.packet.in.handler.interface_actions;

import com.rageps.content.Attributes;
import com.rageps.content.market.MarketShop;
import com.rageps.content.skill.crafting.JewelleryMoulding;
import com.rageps.content.skill.smithing.Smithing;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.in.model.interface_actions.FirstItemInterfacePacket;
import com.rageps.net.refactor.packet.in.model.interface_actions.SecondItemInterfacePacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.activity.ActivityManager;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.container.session.ExchangeSession;
import com.rageps.world.entity.item.container.session.ExchangeSessionManager;
import com.rageps.world.entity.item.container.session.ExchangeSessionType;

import java.util.Optional;

/**
 * todo - check verificiation if a player doesn't have item
 */
public class SecondItemInterfacePacketHandler implements PacketHandler<SecondItemInterfacePacket> {

    @Override
    public void handle(Player player, SecondItemInterfacePacket packet) {
        int interfaceId = packet.getInterfaceID();
        int slot = packet.getSlot();
        int itemId = packet.getItemId();

        if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_INTERFACE))
            return;

        if(interfaceId < 0 || slot < 0 || itemId < 0)
            return;

        if (interfaceId == MarketShop.INVENTORY_CONTAINER_ID) {
            if (player.getMarketShop() != null) {
                player.getMarketShop().sell(player, new Item(itemId, 1), slot);
            }
        }

        if (interfaceId == MarketShop.SHOP_CONTAINER_ID) {
            if (player.getMarketShop() != null) {
                player.getMarketShop().purchase(player, new Item(itemId, 1));
            }
        }
        if (Attributes.secondSlot(player, interfaceId, slot)) {
            return;
        }
        if (JewelleryMoulding.mould(player, itemId, 5)) {
            return;
        }
        if (Smithing.forge(player, interfaceId, slot, 5)) {
            return;
        }
        Optional<ExchangeSession> session = ExchangeSessionManager.get().getExchangeSession(player);
        switch (interfaceId) {
            case 3322:
                if (!session.isPresent()) {
                    return;
                }
                if (session.get().getType().equals(ExchangeSessionType.TRADE)) {
                    session.get().add(player, slot, 5);
                } else if (session.get().getType().equals(ExchangeSessionType.DUEL)) {
                    session.get().add(player, slot, 5);
                }
                break;
            case 3415:
                if (!session.isPresent()) {
                    return;
                }
                if (session.get().getType().equals(ExchangeSessionType.TRADE)) {
                    session.get().remove(player, new Item(itemId, 5));
                }
                break;
            case 6669:
                if (!session.isPresent()) {
                    return;
                }
                if (session.get().getType().equals(ExchangeSessionType.DUEL)) {
                    session.get().remove(player, new Item(itemId, 5));
                }
                break;
        }
        player.getActivityManager().execute(ActivityManager.ActivityType.ITEM_INTERFACE);
    }
}