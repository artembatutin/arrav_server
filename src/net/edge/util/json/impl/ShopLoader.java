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
 */
public final class ShopLoader extends JsonLoader {
	
	/**
	 * Creates a new {@link ShopLoader}.
	 */
	public ShopLoader() {
		super("./data/def/item/market_shops.json");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		int id = Objects.requireNonNull(reader.get("id").getAsInt());
		String name = Objects.requireNonNull(reader.get("name").getAsString());
		boolean iron = reader.get("iron").getAsBoolean();
		int[] items = builder.fromJson(reader.get("items").getAsJsonArray(), int[].class);
		Currency currency = Objects.requireNonNull(Currency.valueOf(reader.get("currency").getAsString()));
		MarketShop shop = new MarketShop(id, name, currency, iron, items);
		MarketCounter.getShops().put(id, shop);
	}
}
