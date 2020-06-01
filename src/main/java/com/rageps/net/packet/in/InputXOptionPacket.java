package com.rageps.net.packet.in;

import com.rageps.content.market.MarketShop;
import com.rageps.content.skill.summoning.Summoning;
import com.rageps.net.codec.ByteOrder;
import com.rageps.net.codec.ByteTransform;
import com.rageps.net.codec.game.GamePacket;
import com.rageps.net.packet.IncomingPacket;
import com.rageps.net.packet.out.SendEnterAmount;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.item.Item;
import com.rageps.world.entity.item.ItemDefinition;
import com.rageps.world.entity.item.container.impl.Bank;
import com.rageps.world.entity.item.container.session.ExchangeSession;
import com.rageps.world.entity.item.container.session.ExchangeSessionManager;
import com.rageps.world.entity.item.container.session.ExchangeSessionType;

public final class InputXOptionPacket implements IncomingPacket {
	
	@Override
	public void handle(Player player, int opcode, int size, GamePacket buf) {
		int slot = buf.getShort(ByteOrder.LITTLE);
		int interfaceId = buf.getShort(ByteTransform.A);
		int itemId = buf.getShort(ByteOrder.LITTLE);
		
		if(slot < 0 || interfaceId < 0 || itemId < 0 || itemId > ItemDefinition.DEFINITIONS.length) {
			return;
		}
		
		player.getAttr().get("enter_x_item_tab").set(0);//bank tab
		player.getAttr().get("enter_x_item_slot").set(slot);
		if(interfaceId == Bank.BANK_INVENTORY_ID && player.getAttr().get("banking").getBoolean()) {
				player.out(new SendEnterAmount("How many you would like to withdraw?", s -> () -> {
					if(player.getAttr().get("banking").getBoolean()) {
						player.getBank().withdraw(player, player.getAttr().get("enter_x_item_tab").getInt(), player.getAttr().get("enter_x_item_slot").getInt(), Integer.parseInt(s));
					}
				}));
		}
		switch(interfaceId) {
			case MarketShop.INVENTORY_CONTAINER_ID:
				if(player.getInventory().get(slot) == null)
					return;
				if(player.getMarketShop() == null)
					return;
				player.getAttr().get("shop_item").set(player.getInventory().get(slot).getId());
				player.out(new SendEnterAmount("How many you would like to sell?", t -> () -> player.getMarketShop().sell(player, new Item(player.getAttr().get("shop_item").getInt(), Integer.parseInt(t)), player.getAttr().get("enter_x_item_slot").getInt())));
				break;

			case MarketShop.SHOP_CONTAINER_ID:
				if(player.getMarketShop() == null)
					return;
				player.getAttr().get("buying_shop_item").set(player.getMarketShop().getItems().getInt(slot));
				player.out(new SendEnterAmount("How many you would like to buy?", t -> () -> {
					if(player.getMarketShop() != null)
					player.getMarketShop().purchase(player, new Item(player.getAttr().get("buying_shop_item").getInt(), Integer.parseInt(t)));
				}));
				break;

			case Bank.SIDEBAR_INVENTORY_ID:
				if(player.getAttr().get("banking").getBoolean()) {
					player.out(new SendEnterAmount("How many you would like to deposit?", t -> () -> {
						int amount = Integer.parseInt(t);
						if(player.getAttr().get("banking").getBoolean()) {
							player.getBank().deposit(player.getAttr().get("enter_x_item_slot").getInt(), amount, player.getInventory(), true);
						}
					}));
					return;
				}
				break;

			case 5064://Beast of burden inventory
				if(player.getAttr().get("bob").getBoolean()) {
					player.out(new SendEnterAmount("How many you would like to store?", t -> () -> {
						int amount = Integer.parseInt(t);
						if(player.getAttr().get("bob").getBoolean()) {
							Summoning.store(player, player.getAttr().get("enter_x_item_slot").getInt(), amount);
						}
					}));
					return;
				}
				break;
			case 3322://Inventory -> trade or duel
				if(ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.TRADE).isPresent() || ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.DUEL).isPresent()) {
					player.out(new SendEnterAmount("How many you would like to deposit?", t -> () -> {
						int amount = Integer.parseInt(t);
						if(ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.TRADE).isPresent()) {
							ExchangeSession session = ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.TRADE).get();
							int slot1 = player.getAttr().get("enter_x_item_slot").getInt();
							session.add(player, slot1, amount);
						} else if(ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.DUEL).isPresent()) {
							ExchangeSession session = ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.DUEL).get();
							int slot1 = player.getAttr().get("enter_x_item_slot").getInt();
							session.add(player, slot1, amount);
						}
					}));
					return;
				}
				break;
			case 2702:
				if(player.getAttr().get("bob").getBoolean()) {
					player.out(new SendEnterAmount("How many you would like to withdraw?", t -> () -> {
						if(player.getAttr().get("bob").getBoolean()) {
							Summoning.withdraw(player, player.getAttr().get("enter_x_item_slot").getInt(), Integer.parseInt(t));
						}
					}));
				}
				break;
			case 6669://Duel -> inventory
				if(ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.DUEL).isPresent()) {
					player.out(new SendEnterAmount("How many you would like to withdraw?", t -> () -> {
						int amount = Integer.parseInt(t);
						if(ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.DUEL).isPresent()) {
							ExchangeSession session = ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.DUEL).get();
							Item item = session.getExchangeSession().get(player).get(player.getAttr().get("enter_x_item_slot").getInt());
							session.remove(player, new Item(item.getId(), amount));
						}
					}));
				}
				break;
			case 3415://Trade -> inventory
				if(ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.TRADE).isPresent()) {
					player.out(new SendEnterAmount("How many you would like to withdraw?", t -> () -> {
						int amount = Integer.parseInt(t);
						if(ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.TRADE).isPresent()) {
							ExchangeSession session = ExchangeSessionManager.get().getExchangeSession(player, ExchangeSessionType.TRADE).get();
							Item item = session.getExchangeSession().get(player).get(player.getAttr().get("enter_x_item_slot").getInt());
							session.remove(player, new Item(item.getId(), amount));
						}
					}));
				}
				break;
		}
	}
	
}
