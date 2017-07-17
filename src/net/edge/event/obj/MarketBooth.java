package net.edge.event.obj;

import net.edge.content.market.MarketCounter;
import net.edge.event.EventInitializer;
import net.edge.event.impl.ObjectEvent;
import net.edge.world.node.actor.player.Player;
import net.edge.world.object.ObjectNode;

public class MarketBooth extends EventInitializer {
	@Override
	public void init() {
		ObjectEvent m = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				player.widget(-13);
				return true;
			}
		};
		m.registerFirst(24124);
		
		//tomatoes crates
		m = new ObjectEvent() {
			@Override
			public boolean click(Player player, ObjectNode object, int click) {
				MarketCounter.getShops().get(5).openShop(player);
				return true;
			}
		};
		m.registerFirst(6839);
	}
}
