package net.arrav.action.but;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ButtonAction;
import net.arrav.content.market.MarketCounter;
import net.arrav.content.market.MarketShop;
import net.arrav.net.packet.out.SendEnterAmount;
import net.arrav.world.entity.actor.player.Player;

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
