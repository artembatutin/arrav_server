package net.edge.action.obj;

import net.edge.content.market.MarketCounter;
import net.edge.action.ActionInitializer;
import net.edge.action.impl.ObjectAction;
import net.edge.world.entity.actor.player.Player;
import net.edge.world.object.ObjectNode;

public class MarketBooth extends ActionInitializer {
	@Override
	public void init() {
		ObjectAction m = new ObjectAction() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.widget(-13);
				return true;
			}
		};
		m.registerFirst(24124);
		
		//tomatoes crates
		m = new ObjectAction() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				MarketCounter.getShops().get(5).openShop(player);
				return true;
			}
		};
		m.registerFirst(6839);
	}
}
