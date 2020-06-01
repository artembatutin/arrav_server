package com.rageps.content.market;

import com.google.gson.Gson;
import com.rageps.content.market.exchange.personal.PlayerCounter;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import com.rageps.util.json.JsonSaver;
import com.rageps.world.entity.actor.player.Player;

/**
 * Controls all the Avarrockian market.
 */
public class MarketCounter {
	
	/**
	 * All cached world shop.
	 */
	private static final Int2ObjectArrayMap<MarketShop> SHOPS = new Int2ObjectArrayMap<>();
	
	/**
	 * All cached player counters.
	 */
	private static final Object2ObjectOpenHashMap<String, PlayerCounter> COUNTERS = new Object2ObjectOpenHashMap<>();
	
	/**
	 * Gets the player specific counter.
	 * @param player the player specific counter.
	 * @return the player counter, creates if not cached.
	 */
	public static PlayerCounter getCounter(Player player) {
		if(!COUNTERS.containsKey(player.credentials.username)) {
			PlayerCounter counter = new PlayerCounter(player);
			COUNTERS.put(player.credentials.username, counter);
		}
		return COUNTERS.get(player.credentials.username);
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
			item_values_saver.current().addProperty("iron", s.ironAccess);
			item_values_saver.current().addProperty("currency", s.getCurrency().name().toUpperCase());
			item_values_saver.current().addProperty("items", new Gson().toJson(s.getItems()));
			item_values_saver.split();
		}
		item_values_saver.publish("./data/def/item/market_shops2.json");
	}
	
	public static Int2ObjectArrayMap<MarketShop> getShops() {
		return SHOPS;
	}
	
	public static Object2ObjectOpenHashMap<String, PlayerCounter> getCounters() {
		return COUNTERS;
	}
}
