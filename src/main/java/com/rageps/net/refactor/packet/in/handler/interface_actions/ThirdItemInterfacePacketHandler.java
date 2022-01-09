package com.rageps.net.refactor.packet.in.handler.interface_actions;

import com.rageps.content.Attributes;
import com.rageps.content.market.MarketShop;
import com.rageps.content.skill.crafting.JewelleryMoulding;
import com.rageps.content.skill.smithing.Smithing;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.in.model.interface_actions.FirstItemInterfacePacket;
import com.rageps.net.refactor.packet.in.model.interface_actions.ThirdItemInterfacePacket;
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
public class ThirdItemInterfacePacketHandler implements PacketHandler<ThirdItemInterfacePacket> {

    @Override
    public void handle(Player player, ThirdItemInterfacePacket packet) {
        int interfaceId = packet.getInterfaceID();
        int slot = packet.getSlot();
        int itemId = packet.getItemId();

        if(player.getActivityManager().contains(ActivityManager.ActivityType.ITEM_INTERFACE))
            return;

        if(interfaceId < 0 || slot < 0 || itemId < 0)
            return;

        if(Attributes.thirdSlot(player, interfaceId, slot)) {
            return;
        }
        if(JewelleryMoulding.mould(player, itemId, 10)) {
            return;
        }
        if(Smithing.forge(player, interfaceId, slot, 10)) {
            return;
        }
        if(interfaceId == MarketShop.SHOP_CONTAINER_ID) {
            if(player.getMarketShop() != null) {
                player.getMarketShop().purchase(player, new Item(itemId, 5));
            }
        }
        Optional<ExchangeSession> session = ExchangeSessionManager.get().getExchangeSession(player);
        switch(interfaceId) {
            case MarketShop.INVENTORY_CONTAINER_ID:
                if(player.getMarketShop() != null)
                    player.getMarketShop().sell(player, new Item(itemId, 5), slot);
                break;
            case 3322:
                if(!session.isPresent()) {
                    return;
                }
                if(session.get().getType().equals(ExchangeSessionType.TRADE)) {
                    session.get().add(player, slot, 10);
                } else if(session.get().getType().equals(ExchangeSessionType.DUEL)) {
                    session.get().add(player, slot, 10);
                }
                break;
            case 3415:
                if(!session.isPresent()) {
                    return;
                }
                if(session.get().getType().equals(ExchangeSessionType.TRADE)) {
                    session.get().remove(player, new Item(itemId, 10));
                }
                break;
            case 6669:
                if(!session.isPresent()) {
                    return;
                }
                if(session.get().getType().equals(ExchangeSessionType.DUEL)) {
                    session.get().remove(player, new Item(itemId, 10));
                }
                break;
        }

        player.getActivityManager().execute(ActivityManager.ActivityType.ITEM_INTERFACE);
    }
}