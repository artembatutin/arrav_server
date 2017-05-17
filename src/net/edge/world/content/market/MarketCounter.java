package net.edge.world.content.market;

import com.google.gson.Gson;
import net.edge.utils.json.JsonSaver;
import net.edge.world.content.market.exchange.personal.PlayerCounter;
import net.edge.world.model.node.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Controls all the Avarrockian market.
 */
public class MarketCounter {
	
	/**
	 * All cached player counters.
	 */
	private static final Map<Integer, MarketShop> SHOPS = new HashMap<>();
	
	/**
	 * All cached player counters.
	 */
	private static final Map<String, PlayerCounter> COUNTERS = new HashMap<>();
	
	/**
	 * Gets the player specific counter.
	 * @param player the player specific counter.
	 * @return the player counter, creates if not cached.
	 */
	public static PlayerCounter getCounter(Player player) {
		if(!COUNTERS.containsKey(player.getUsername())) {
			PlayerCounter counter = new PlayerCounter(player);
			COUNTERS.put(player.getUsername(), counter);
		}
		return COUNTERS.get(player.getUsername());
	}
	
	/**
	 * Serializes the shops.
	 */
	public static void serializeShops() {
		JsonSaver item_values_saver = new JsonSaver();
		for(int id : SHOPS.keySet()) {
			MarketShop s = SHOPS.get(id);
			if(s == null) {
				continue;
			}
			
			item_values_saver.current().addProperty("id", id);
			item_values_saver.current().addProperty("name", s.getTitle());
			item_values_saver.current().addProperty("currency", s.getCurrency().name().toUpperCase());
			item_values_saver.current().addProperty("items", new Gson().toJson(s.getItems()));
			item_values_saver.split();
		}
		item_values_saver.publish("./data/json/items/market_shops2.json");
	}
	
	public static Map<Integer, MarketShop> getShops() {
		return SHOPS;
	}
	
	public static Map<String, PlayerCounter> getCounters() {
		return COUNTERS;
	}
}
