package com.rageps.action.but;

import com.rageps.action.impl.ButtonAction;
import com.rageps.content.market.MarketCounter;
import com.rageps.content.market.MarketShop;
import com.rageps.action.ActionInitializer;
import com.rageps.net.packet.out.SendEnterAmount;
import com.rageps.world.entity.actor.player.Player;

public class PanelShopEditButton extends ActionInitializer {
	
	@Override
	public void init() {
		ButtonAction e = new ButtonAction() {
			@Override
			public boolean click(Player player, int button) {
				player.out(new SendEnterAmount("Item id:", t -> () -> {
					int id = Integer.parseInt(t);
					if(player.getMarketShop() != null && player.getMarketShop().getId() != -1) {
						int shopId = player.getMarketShop().getId();
						MarketShop shop = MarketCounter.getShops().get(shopId);
						shop.getItems().add(id);
						shop.openShop(player);
					}
				}));
				return true;
			}
		};
		e.register(124);
		
	}
	
}
