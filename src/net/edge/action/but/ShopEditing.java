package net.edge.action.but;

import net.edge.action.ActionInitializer;
import net.edge.action.impl.ButtonAction;
import net.edge.content.market.MarketCounter;
import net.edge.content.market.MarketShop;
import net.edge.net.packet.out.SendEnterAmount;
import net.edge.world.entity.actor.player.Player;

public class ShopEditing extends ActionInitializer {
	
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
