package com.rageps.net.refactor.packet.in.handler.interface_actions;

import com.rageps.content.Attributes;
import com.rageps.content.market.MarketShop;
import com.rageps.content.skill.crafting.JewelleryMoulding;
import com.rageps.content.skill.smithing.Smithing;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.in.model.interface_actions.FirstItemInterfacePacket;
import com.rageps.net.refactor.packet.in.model.interface_actions.FourthItemInterfacePacket;
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
public class FourthItemInterfacePacketHandler implements PacketHandler<FourthItemInterfacePacket> {

    @Override
    public void handle(Player player, FourthItemInterfacePacket packet) {
        int interfaceId = packet.getInterfaceID();
        int slot = packet.getSlot();
        int itemId = packet.getItemId();

        if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_INTERFACE))
            return;

        if(interfaceId < 0 || slot < 0 || itemId < 0)
            return;

        if(interfaceId == MarketShop.INVENTORY_CONTAINER_ID) {
            if(player.getMarketShop() != null) {
                player.getMarketShop().sell(player, new Item(itemId, 10), slot);
            }
        }
        if(interfaceId == MarketShop.SHOP_CONTAINER_ID) {
            if(player.getMarketShop() != null) {
                player.getMarketShop().purchase(player, new Item(itemId, 10));
            }
        }
        if(Attributes.fourthSlot(player, interfaceId, itemId, slot)) {
            return;
        }
        Optional<ExchangeSession> session = ExchangeSessionManager.get().getExchangeSession(player);
        switch(interfaceId) {
            case 3322:
                if(!session.isPresent()) {
                    return;
                }
                if(session.get().getType().equals(ExchangeSessionType.TRADE)) {
                    session.get().add(player, slot, player.getInventory().computeAmountForId(itemId));
                } else if(session.get().getType().equals(ExchangeSessionType.DUEL)) {
                    session.get().add(player, slot, player.getInventory().computeAmountForId(itemId));
                }
                break;
            case 3415:
                if(!session.isPresent()) {
                    return;
                }
                if(session.get().getType().equals(ExchangeSessionType.TRADE)) {
                    session.get().remove(player, new Item(itemId, session.get().getExchangeSession().get(player).computeAmountForId(itemId)));
                }
                break;
            case 6669:
                if(!session.isPresent()) {
                    return;
                }
                if(session.get().getType().equals(ExchangeSessionType.DUEL)) {
                    session.get().remove(player, new Item(itemId, player.getInventory().computeAmountForId(itemId)));
                }
                break;
        }
        player.getActivityManager().execute(ActivityManager.ActivityType.ITEM_INTERFACE);
    }
}