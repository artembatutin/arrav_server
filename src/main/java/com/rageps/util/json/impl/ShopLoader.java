package com.rageps.util.json.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rageps.content.market.MarketCounter;
import com.rageps.content.market.MarketShop;
import com.rageps.content.market.currency.Currency;
import com.rageps.util.json.JsonLoader;

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
