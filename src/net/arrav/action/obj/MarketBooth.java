package net.arrav.action.obj;

import net.arrav.action.ActionInitializer;
import net.arrav.action.impl.ObjectAction;
import net.arrav.content.market.MarketCounter;
import net.arrav.world.entity.actor.player.Player;
import net.arrav.world.object.GameObject;

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
