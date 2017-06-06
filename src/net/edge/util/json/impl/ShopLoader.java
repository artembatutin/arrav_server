package net.edge.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.edge.util.json.JsonLoader;
import net.edge.content.market.MarketCounter;
import net.edge.content.market.MarketShop;
import net.edge.content.market.currency.Currency;

import java.util.Objects;

/**
 * The {@link JsonLoader} implementation that loads all shops.
 * @author lare96 <http://github.com/lare96>
 */
public final class ShopLoader extends JsonLoader {
	
	/**
	 * Creates a new {@link ShopLoader}.
	 */
	public ShopLoader() {
		super("./data/json/items/market_shops.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		int id = Objects.requireNonNull(reader.get("id").getAsInt());
		String name = Objects.requireNonNull(reader.get("name").getAsString());
		int[] items = builder.fromJson(reader.get("items").getAsJsonArray(), int[].class);
		Currency currency = Objects.requireNonNull(Currency.valueOf(reader.get("currency").getAsString()));
		
		MarketShop shop = new MarketShop(name, currency, items);
		if(MarketCounter.getShops().containsKey(id))
			throw new IllegalStateException("Duplicate shop id: " + id);
		MarketCounter.getShops().put(id, shop);
	}
}
