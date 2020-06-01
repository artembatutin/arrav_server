package com.rageps.action.obj;

import com.rageps.action.impl.ObjectAction;
import com.rageps.content.market.MarketCounter;
import com.rageps.action.ActionInitializer;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.object.GameObject;

public class MarketBooth extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction m = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				player.widget(-13);
				return true;
			}
		};
		m.registerFirst(24124);
		
		//tomatoes crates
		m = new ObjectAction() {
			@Override
			public boolean click(Player player, GameObject object, int click) {
				MarketCounter.getShops().get(5).openShop(player);
				return true;
			}
		};
		m.registerFirst(6839);
	}
}
