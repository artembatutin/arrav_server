package com.rageps.net.refactor.packet.in.handler;

import com.rageps.content.market.MarketShop;
import com.rageps.content.skill.summoning.Summoning;
import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.refactor.packet.in.model.InputXOptionPacketPacket;
import com.rageps.net.refactor.packet.PacketHandler;
import com.rageps.net.refactor.packet.out.model.EnterAmountPacket;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.PlayerAttributes;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.ItemDefinition;
import com.rageps.world.entity.item.container.impl.Bank;
import com.rageps.world.entity.item.container.session.ExchangeSession;
import com.rageps.world.entity.item.container.session.ExchangeSessionManager;
import com.rageps.world.entity.item.container.session.ExchangeSessionType;

/**
 * @author Tamatea <tamateea@gmail.com>
 */
public class InputXOptionPacketPacketHandler implements PacketHandler<InputXOptionPacketPacket> {

    @Override
    public void handle(Player player, InputXOptionPacketPacket packet) {
        int slot = packet.getSlot();
        int interfaceId = packet.getInterfaceId();
        int itemId = packet.getItemId();

        if(slot < 0 || interfaceId < 0 || itemId < 0 || itemId > ItemDefinition.DEFINITIONS.length) {
            return;
        }

        player.getAttributeMap().set(PlayerAttributes.ENTER_X_ITEM_TAB, 0);//bank tab
        player.getAttributeMap().set(PlayerAttributes.ENTER_X_ITEM_SLOT, slot);
        if(interfaceId == Bank.BANK_INVENTORY_ID && player.getAttributeMap().getBoolean(PlayerAttributes.BANKING)) {
            player.send(new EnterAmountPacket(player, "How many you would like to withdraw?", s -> () -> {
                if(player.getAttributeMap().getBoolean(PlayerAttributes.BANKING)) {
                    player.getBank().withdraw(player, player.getAttributeMap().getInt(PlayerAttributes.ENTER_X_ITEM_TAB), player.getAttributeMap().getInt(PlayerAttributes.ENTER_X_ITEM_SLOT), Integer.parseInt(s));
                }
            }));
        }
        switch(interfaceId) {
            case MarketShop.INVENTORY_CONTAINER_ID:
                if(player.getInventory().get(slot) == null)
                    return;
                if(player.getMarketShop() == null)
                    return;
                player.getAttributeMap().set(PlayerAttributes.SHOP_ITEM, player.getInventory().get(slot).getId());
                player.send(new EnterAmountPacket(player, "How many you would like to sell?", t -> () -> player.getMarketShop().sell(player, new Item(player.getAttributeMap().getInt(PlayerAttributes.SHOP_ITEM), Integer.parseInt(t)), player.getAttributeMap().getInt(PlayerAttributes.ENTER_X_ITEM_SLOT))));
                break;

            case MarketShop.SHOP_CONTAINER_ID:
                if(player.getMarketShop() == null)
                    return;
                player.getAttributeMap().set(PlayerAttributes.BUYING_SHOP_ITEM, player.getMarketShop().getItems().getInt(slot));
                player.send(new EnterAmountPacket(player, "How many you would like to buy?", t -> () -> {
                    if(player.getMarketShop() != null)
                        player.getMarketShop().purchase(player, new Item(player.getAttributeMap().getInt(PlayerAttributes.BUYING_SHOP_ITEM), Integer.parseInt(t)));
                }));
                break;

            case Bank.SIDEBAR_INVENTORY_ID:
                if(player.getAttributeMap().getBoolean(PlayerAttributes.BANKING)) {
                    player.send(new EnterAmountPacket(player, "How many you would like to deposit?", t -> () -> {
                        int amount = Integer.parseInt(t);
                        if(player.getAttributeMap().getBoolean(PlayerAttributes.BANKING)) {
                            player.getBank().deposit(player.getAttributeMap().getInt(PlayerAttributes.ENTER_X_ITEM_SLOT), amount, player.getInventory(), true);
                        }
                    }));
                    return;
                }
                break;

            case 5064://Beast of burden inventory
                if(player.getAttributeMap().getBoolean(PlayerAttributes.BOB)) {
                    player.send(new EnterAmountPacket(player, "How many you would like to store?", t -> () -> {
                        int amount = Integer.parseInt(t);
                        if(player.getAttributeMap().getBoolean(PlayerAttributes.BOB)) {
                            Summoning.store(player, player.getAttributeMap().getInt(PlayerAttributes.ENTER_X_ITEM_SLOT), amount);
                        }
                    }));
                    return;
                }
                break;
            case 3322://Inventory -> trade or duel
                if(ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.TRADE).isPresent() || ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.DUEL).isPresent()) {
                    player.send(new EnterAmountPacket(player, "How many you would like to deposit?", t -> () -> {
                        int amount = Integer.parseInt(t);
                        if(ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.TRADE).isPresent()) {
                            ExchangeSession session = ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.TRADE).get();
                            int slot1 = player.getAttributeMap().getInt(PlayerAttributes.ENTER_X_ITEM_SLOT);
                            session.add(player, slot1, amount);
                        } else if(ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.DUEL).isPresent()) {
                            ExchangeSession session = ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.DUEL).get();
                            int slot1 = player.getAttributeMap().getInt(PlayerAttributes.ENTER_X_ITEM_SLOT);
                            session.add(player, slot1, amount);
                        }
                    }));
                    return;
                }
                break;
            case 2702:
                if(player.getAttributeMap().getBoolean(PlayerAttributes.BOB)) {
                    player.send(new EnterAmountPacket(player, "How many you would like to withdraw?", t -> () -> {
                        if(player.getAttributeMap().getBoolean(PlayerAttributes.BOB)) {
                            Summoning.withdraw(player, player.getAttributeMap().getInt(PlayerAttributes.ENTER_X_ITEM_SLOT), Integer.parseInt(t));
                        }
                    }));
                }
                break;
            case 6669://Duel -> inventory
                if(ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.DUEL).isPresent()) {
                    player.send(new EnterAmountPacket(player, "How many you would like to withdraw?", t -> () -> {
                        int amount = Integer.parseInt(t);
                        if(ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.DUEL).isPresent()) {
                            ExchangeSession session = ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.DUEL).get();
                            Item item = session.getExchangeSession().get(player).get(player.getAttributeMap().getInt(PlayerAttributes.ENTER_X_ITEM_SLOT));
                            session.remove(player, new Item(item.getId(), amount));
                        }
                    }));
                }
                break;
            case 3415://Trade -> inventory
                if(ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.TRADE).isPresent()) {
                    player.send(new EnterAmountPacket(player, "How many you would like to withdraw?", t -> () -> {
                        int amount = Integer.parseInt(t);
                        if(ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.TRADE).isPresent()) {
                            ExchangeSession session = ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.TRADE).get();
                            Item item = session.getExchangeSession().get(player).get(player.getAttributeMap().getInt(PlayerAttributes.ENTER_X_ITEM_SLOT));
                            session.remove(player, new Item(item.getId(), amount));
                        }
                    }));
                }
                break;
        }
    }
}